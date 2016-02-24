package com.youxing.sogoteacher.apply;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lling.photopicker.PhotoPickerActivity;
import com.youxing.common.adapter.GroupStyleAdapter;
import com.youxing.common.app.Constants;
import com.youxing.common.model.BaseModel;
import com.youxing.common.model.UploadImageModel;
import com.youxing.common.services.account.AccountService;
import com.youxing.common.services.http.CacheType;
import com.youxing.common.services.http.HttpService;
import com.youxing.common.services.http.RequestHandler;
import com.youxing.common.utils.Log;
import com.youxing.common.utils.UnitTools;
import com.youxing.common.views.CircularImage;
import com.youxing.sogoteacher.R;
import com.youxing.sogoteacher.app.SGActivity;
import com.youxing.sogoteacher.apply.views.AddExpItem;
import com.youxing.sogoteacher.model.AccountModel;
import com.youxing.sogoteacher.model.ApplyTeacherModel;
import com.youxing.sogoteacher.model.Education;
import com.youxing.sogoteacher.model.Experience;
import com.youxing.sogoteacher.utils.PhotoPicker;
import com.youxing.sogoteacher.views.InputListItem;
import com.youxing.sogoteacher.views.SectionView;
import com.youxing.sogoteacher.views.SimpleListItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 成为助教
 *
 * Created by Jun Deng on 16/1/11.
 */
public class ApplyTeacherActivity extends SGActivity implements AdapterView.OnItemClickListener, InputListItem.InputChangeListener {

    private static final int REQUEST_CODE_PICK_PHOTO = 1;
    private static final int REQUEST_CODE_EDIT_EXP = 2;
    private static final int REQUEST_CODE_EDIT_EDU = 3;

    private Adapter adapter;
    private PhotoPicker photoPicker;

    private ApplyTeacherModel model;
    private int status;
    private boolean fromLogin; //是否是从第一次打开App登录后跳进来的

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        fromLogin = Boolean.valueOf(getIntent().getData().getQueryParameter("fromLogin"));

        ListView listView = (ListView) findViewById(R.id.listView);
        adapter = new Adapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        requestData();
    }

    public void requestData() {
        showLoadingDialog(this);

        HttpService.get(Constants.domain() + "/teacher/status", null, CacheType.DISABLE, ApplyTeacherModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                ApplyTeacherActivity.this.model = (ApplyTeacherModel) response;

                // 0 未提交过，1 审核通过，2 信息不完整，3 等待审核，4 简历审核未通过， 5 面试审核未通过，6 面试中
                status = model.getData().getStatus();
                if (status == 1) {
                    setTitle("助教信息");

                } else if (status == 3 || status == 6) {
                    showEmptyView("您的申请已经提交，正在审核中，请耐心等待1~2个工作日哦~");

                } else {
                    if (status == 4) {
                        showDialog(ApplyTeacherActivity.this, "对不起，您的简历审核未通过");
                    } else if (status == 5) {
                        showDialog(ApplyTeacherActivity.this, "对不起，您的面试审核未通过");
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(ApplyTeacherActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        });
    }

    private void submit() {
        final boolean isContentFull = check();
        if (!fromLogin && !isContentFull) {
            showDialog(ApplyTeacherActivity.this, "个人资料填写不完整，请完善后重新提交！", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            return;
        }
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("teacher", JSON.toJSONString(model.getData())));
        HttpService.post(Constants.domain() + "/teacher/signup", params, BaseModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                if (isContentFull) {
                    showDialog(ApplyTeacherActivity.this, "申请助教成功，请耐心等待审核哦~", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                } else {
                    showDialog(ApplyTeacherActivity.this, null, "个人资料填写不完整，可以在“我的-成为助教”中完善后重新提交", "继续填写", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }, "跳过", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                }
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(ApplyTeacherActivity.this, error.getErrmsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
            }
        });
    }

    private boolean check() {
        if (TextUtils.isEmpty(model.getData().getPic())) {
            return false;

        } else if (TextUtils.isEmpty(model.getData().getName())) {
            return false;

        } else if (TextUtils.isEmpty(model.getData().getIdNo())) {
            return false;

        } else if (TextUtils.isEmpty(model.getData().getSex())) {
            return false;

        } else if (TextUtils.isEmpty(model.getData().getBirthday())) {
            return false;

        } else if (TextUtils.isEmpty(model.getData().getAddress())) {
            return false;

        } else if (model.getData().getExperiences().size() == 0) {
            return false;

        } else if (model.getData().getEducations().size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (status == 1) {
            return;
        }

        GroupStyleAdapter.IndexPath indexPath = adapter.getIndexForPosition(position);
        int section = indexPath.section;
        int row = indexPath.row;

        if (section == 0) {
            if (row == 0) {
                pickPhoto();
            } else if (row == 3) {
                chooseSex();
            } else if (row == 4) {
                selectBirthday();
            }

        } else if (section == 1) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("sgteacher://editexp"));
            if (row < model.getData().getExperiences().size()) {
                Experience exp = model.getData().getExperiences().get(row);
                i.putExtra("exp", exp);
            }
            startActivityForResult(i, REQUEST_CODE_EDIT_EXP);

        } else {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("sgteacher://editedu"));
            if (row < model.getData().getEducations().size()) {
                Education edu = model.getData().getEducations().get(row);
                i.putExtra("edu", edu);
            }
            startActivityForResult(i, REQUEST_CODE_EDIT_EDU);
        }
    }

    @Override
    public void onInputChanged(InputListItem inputListItem, String textNow) {
        int row = ((Integer) inputListItem.getTag()).intValue();
        if (row == 1) {
            model.getData().setName(textNow);
        } else if (row == 2) {
            model.getData().setIdNo(textNow);
        } else {
            model.getData().setAddress(textNow);
        }
    }

    private void pickPhoto() {
        Intent intent = new Intent(ApplyTeacherActivity.this, PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_SINGLE);
        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 1);
        startActivityForResult(intent, REQUEST_CODE_PICK_PHOTO);
    }

    private void chooseSex() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ApplyTeacherActivity.this);
        builder.setItems(new String[]{"男", "女"}, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        model.getData().setSex("男");
                        break;

                    case 1:
                        model.getData().setSex("女");
                        break;
                }
                adapter.notifyDataSetChanged();
            }
        });
        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    private void selectBirthday() {
        // 生日
        Calendar cal = Calendar.getInstance();
        if (!TextUtils.isEmpty(model.getData().getBirthday())) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date date = dateFormat.parse(model.getData().getBirthday());
                cal.setTime(date);
            } catch (Exception e) {
                Log.e("PersonInfoActivity", "parse birthday fail", e);
            }
        }
        new DatePickerDialog(ApplyTeacherActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String birthday = dateFormat.format(cal.getTime());

                model.getData().setBirthday(birthday);
                adapter.notifyDataSetChanged();
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                if (result.size() > 0) {
                    String strImgPath = result.get(0);
                    if (photoPicker == null) {
                        photoPicker = new PhotoPicker(ApplyTeacherActivity.this);
                    }
                    requestUploadImage(new File(strImgPath));
                }
            }
        } else if (requestCode == REQUEST_CODE_EDIT_EXP) {
            if (resultCode == RESULT_OK) {
                Experience exp = data.getParcelableExtra("exp");
                boolean remove = data.getBooleanExtra("remove", false);

                if (remove) { // 删除操作
                    List newList = new ArrayList();
                    for (Experience experience : model.getData().getExperiences()) {
                        if (experience.getId() != exp.getId()) {
                            newList.add(experience);
                        }
                    }
                    model.getData().setExperiences(newList);
                    adapter.notifyDataSetChanged();

                } else { // 保存操作
                    boolean isNew = true;
                    for (Experience experience : model.getData().getExperiences()) {
                        if (experience.getId() == exp.getId()) {
                            experience.setSchool(exp.getSchool());
                            experience.setContent(exp.getContent());
                            experience.setPost(exp.getPost());
                            experience.setTime(exp.getTime());
                            isNew = false;
                            break;
                        }
                    }
                    if (isNew) {
                        List newList = new ArrayList(model.getData().getExperiences());
                        newList.add(0, exp);
                        model.getData().setExperiences(newList);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

        } else if (requestCode == REQUEST_CODE_EDIT_EDU) {
            if (resultCode == RESULT_OK) {
                Education edu = data.getParcelableExtra("edu");
                boolean remove = data.getBooleanExtra("remove", false);

                if (remove) { // 删除操作
                    List newList = new ArrayList();
                    for (Education education : model.getData().getEducations()) {
                        if (education.getId() != edu.getId()) {
                            newList.add(education);
                        }
                    }
                    model.getData().setEducations(newList);
                    adapter.notifyDataSetChanged();

                } else { // 保存操作
                    boolean isNew = true;
                    for (Education education : model.getData().getEducations()) {
                        if (education.getId() == edu.getId()) {
                            education.setSchool(edu.getSchool());
                            education.setMajor(edu.getMajor());
                            education.setLevel(edu.getLevel());
                            education.setTime(edu.getTime());
                            isNew = false;
                            break;
                        }
                    }
                    if (isNew) {
                        List newList = new ArrayList(model.getData().getEducations());
                        newList.add(0, edu);
                        model.getData().setEducations(newList);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    private void requestUploadImage(File file) {
        showLoadingDialog(this);

        HttpService.uploadImage(file, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
//                dismissDialog();
                UploadImageModel uploadImageModel = (UploadImageModel) response;
                model.getData().setPic(uploadImageModel.getData().getPath());
//                adapter.notifyDataSetChanged();

                requestUpdateAvatar(uploadImageModel.getData().getPath());
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(ApplyTeacherActivity.this, error.getErrmsg());
            }
        });
    }

    private void requestUpdateAvatar(String url) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("avatar", url));
        HttpService.post(Constants.domain() + "/user/avatar", params, AccountModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();

                AccountModel model = (AccountModel) response;
                AccountService.instance().dispatchAccountChanged(model.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onRequestFailed(BaseModel error) {
                dismissDialog();
                showDialog(ApplyTeacherActivity.this, error.getErrmsg());
            }
        });
    }

    class Adapter extends GroupStyleAdapter {

        public Adapter() {
            super(ApplyTeacherActivity.this);
        }

        @Override
        public int getSectionCount() {
            if (model == null || status == 3 || status == 6) {
                return 0;
            }
            return 4;
        }

        @Override
        public int getCountInSection(int section) {
            if (section == 0) {
                return 6;

            } else if (section == 1) {
                if (status == 1) {
                    return model.getData().getExperiences().size();
                }
                return 1 + model.getData().getExperiences().size();

            } else if (section == 2) {
                if (status == 1) {
                    return model.getData().getEducations().size();
                }
                return 1 + model.getData().getEducations().size();
            }
            return 0;
        }

        @Override
        public View getViewForRow(View convertView, ViewGroup parent, int section, int row) {
            if (section == 0) {
                if (row == 0 || row == 3 || row == 4) {
                    if (row == 0) {
                        View view = LayoutInflater.from(ApplyTeacherActivity.this).inflate(R.layout.layout_personinfo_avatar_item, null);
                        TextView title = (TextView) view.findViewById(R.id.title);
                        title.setText("生活照");

                        CircularImage avatar = (CircularImage) view.findViewById(R.id.avatar);
                        avatar.setDefaultImageResId(R.drawable.ic_default_avatar);
                        if (!TextUtils.isEmpty(AccountService.instance().account().getAvatar())) {
                            avatar.setImageUrl(AccountService.instance().account().getAvatar());
                        } else {
                            avatar.setImageUrl(model.getData().getPic());
                        }

                        view.findViewById(R.id.arrow).setVisibility(status == 1 ? View.GONE : View.VISIBLE);

                        return view;

                    } else {
                        SimpleListItem listItem = SimpleListItem.create(ApplyTeacherActivity.this);
                        if (row == 3) {
                            listItem.setTitle("性别");
                            listItem.setSubTitle(model.getData().getSex());
                        } else {
                            listItem.setTitle("生日");
                            listItem.setSubTitle(model.getData().getBirthday());
                        }
                        listItem.setShowArrow(status != 1);
                        return listItem;
                    }

                } else {
                    InputListItem listItem = InputListItem.create(ApplyTeacherActivity.this);
                    if (row == 1) {
                        listItem.setTitle("真实姓名");
                        listItem.setInputHint("请输入您的真实姓名");
                        listItem.setInputText(model.getData().getName());

                    } else if (row == 2) {
                        listItem.setTitle("身份证号");
                        listItem.setInputHint("请输入您的身份证号");
                        listItem.setInputText(model.getData().getIdNo());

                    } else {
                        listItem.setTitle("住址");
                        listItem.setInputHint("请输入您的现居地址");
                        listItem.setInputText(model.getData().getAddress());
                    }
                    listItem.setTag(new Integer(row));
                    listItem.setInputChangeListener(ApplyTeacherActivity.this);
                    listItem.setInputEditable(status != 1);
                    return listItem;
                }

            } else if (section == 1) {
                if (row < model.getData().getExperiences().size()) {
                    Experience exp = model.getData().getExperiences().get(row);
                    SimpleListItem listItem = SimpleListItem.create(ApplyTeacherActivity.this);
                    listItem.setTitle(exp.getSchool());
                    listItem.setSubTitle(exp.getTime());
                    listItem.setShowArrow(status != 1);
                    return listItem;

                } else {
                    AddExpItem addExpItem = AddExpItem.create(ApplyTeacherActivity.this);
                    addExpItem.setTitle("+添加工作经历");
                    return addExpItem;
                }

            } else {
                if (row < model.getData().getEducations().size()) {
                    Education edu = model.getData().getEducations().get(row);
                    SimpleListItem listItem = SimpleListItem.create(ApplyTeacherActivity.this);
                    listItem.setTitle(edu.getSchool());
                    listItem.setSubTitle(edu.getTime());
                    listItem.setShowArrow(status != 1);
                    return listItem;

                } else {
                    AddExpItem addExpItem = AddExpItem.create(ApplyTeacherActivity.this);
                    addExpItem.setTitle("+添加教育经历");
                    return addExpItem;
                }
            }
        }

        @Override
        public View getViewForSection(View convertView, ViewGroup parent, int section) {
            if (status == 1 && section == 0) {
                SectionView sectionView = SectionView.create(ApplyTeacherActivity.this);
                sectionView.setTitle("恭喜您！通过助教资格审核，您可以在课程管理中查看课程安排啦~");
                return sectionView;

            } else if (status != 1 && section == getSectionCount() - 1) {
                LinearLayout ll = new LinearLayout(ApplyTeacherActivity.this);
                int padding = UnitTools.dip2px(ApplyTeacherActivity.this, 20);
                ll.setPadding(padding, padding, padding, padding);
                Button payBtn = new Button(ApplyTeacherActivity.this);
                payBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                payBtn.setText("提交");
                payBtn.setTextSize(18);
                payBtn.setTextColor(getResources().getColor(R.color.white));
                payBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        submit();
                    }
                });
                padding = UnitTools.dip2px(ApplyTeacherActivity.this, 10);
                payBtn.setPadding(padding, padding, padding, padding);
                payBtn.setBackgroundResource(R.drawable.btn_shape_green);
                ll.addView(payBtn);
                return ll;
            }
            return super.getViewForSection(convertView, parent, section);
        }
    }

}
