package com.youxing.sogoteacher.apply;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
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
import com.youxing.common.app.Enviroment;
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
 * Created by Jun Deng on 16/1/11.
 */
public class ApplyTeacherActivity extends SGActivity implements AdapterView.OnItemClickListener, InputListItem.InputChangeListener {

    private static final int PICK_PHOTO = 1;

    private Adapter adapter;
    private PhotoPicker photoPicker;

    private ApplyTeacherModel model;
    private int status;
    private boolean fromLogin; //是否是从第一次打开App登录后跳进来的
    private Bitmap picBmp;

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
        showLoadingDialog(this);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("teacher", JSON.toJSONString(model.getData())));
        HttpService.post(Constants.domain() + "/teacher/signup", params, BaseModel.class, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                showDialog(ApplyTeacherActivity.this, "申请助教成功，请耐心等待审核哦~", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
//        if (photoPicker == null) {
//            photoPicker = new PhotoPicker(ApplyTeacherActivity.this);
//        }
//        AlertDialog.Builder builder = new AlertDialog.Builder(ApplyTeacherActivity.this);
//        builder.setItems(new String[]{"拍照", "从手机相册里选择"}, new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                switch (which) {
//                    case 0:
//                        // take a new photo
//                        photoPicker.doTakePhoto();
//                        break;
//
//                    case 1:
//                        // pick a photo from gallery
//                        photoPicker.doPickPhotoFromGallery();
//                        break;
//                }
//            }
//        });
//        Dialog dialog = builder.create();
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.show();

        Intent intent = new Intent(ApplyTeacherActivity.this, PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_SINGLE);
        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 1);
        startActivityForResult(intent, PICK_PHOTO);
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
//        if (resultCode == RESULT_OK) {
//            String strImgPath;
//            switch (requestCode) {
//                case PhotoPicker.REQUEST_CODE_CAMERA:
//                    strImgPath = photoPicker.strImgPath();
//                    if (!TextUtils.isEmpty(strImgPath)) {
//                        File f = new File(strImgPath);
//                        if (!f.exists()) {
//                            strImgPath = photoPicker.parseImgPath(data);
//                        }
//
//                    } else {
//                        strImgPath = photoPicker.parseImgPath(data);
//                    }
//                    if (!TextUtils.isEmpty(strImgPath)) {
//                        photoPicker.doCropPhoto();
//                    }
//                    break;
//                case PhotoPicker.REQUEST_CODE_PHOTO_PICKED:
//                    if (Build.VERSION.SDK_INT < 19) {
//                        strImgPath = photoPicker.parseImgPath(data);
//                        if (!TextUtils.isEmpty(strImgPath)) {
//                            requestUploadImage(new File(strImgPath));
//                            this.picBmp = photoPicker.parseThumbnail(strImgPath);
//                        }
//                    } else {
//                        if (data != null) {
//                            strImgPath = photoPicker.getPath(ApplyTeacherActivity.this, data.getData());
//                            this.picBmp = photoPicker.parseThumbnail(strImgPath);
//                            requestUploadImage(new File(strImgPath));
//                        }
//                    }
//                    break;
//            }
//        }

        if(requestCode == PICK_PHOTO){
            if(resultCode == RESULT_OK){
                ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                if (result.size() > 0) {
                    String strImgPath = result.get(0);
                    if (photoPicker == null) {
                        photoPicker = new PhotoPicker(ApplyTeacherActivity.this);
                    }
                    this.picBmp = photoPicker.parseThumbnail(strImgPath);
                    requestUploadImage(new File(strImgPath));
                }
            }
        }
    }

    private void requestUploadImage(File file) {
        showLoadingDialog(this);

        HttpService.uploadImage(file, new RequestHandler() {
            @Override
            public void onRequestFinish(Object response) {
                dismissDialog();
                UploadImageModel uploadImageModel = (UploadImageModel) response;
                model.getData().setPic(uploadImageModel.getData().getPath());
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
                        if (picBmp != null) {
                            avatar.setImageBitmap(picBmp);

                        } else {
                            avatar.setDefaultImageResId(R.drawable.ic_default_avatar);
                            avatar.setImageUrl(model.getData().getPic());
                        }

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
                        listItem.setShowArrow(true);
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
                    return listItem;
                }

            } else if (section == 1) {
                if (row < model.getData().getExperiences().size()) {
                    Experience exp = model.getData().getExperiences().get(row);
                    SimpleListItem listItem = SimpleListItem.create(ApplyTeacherActivity.this);
                    listItem.setTitle(exp.getSchool());
                    listItem.setSubTitle(exp.getTime());
                    listItem.setShowArrow(true);
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
                    listItem.setShowArrow(true);
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
            if (section == getSectionCount() - 1) {
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
