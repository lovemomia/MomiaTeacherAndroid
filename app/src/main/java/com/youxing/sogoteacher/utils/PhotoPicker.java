package com.youxing.sogoteacher.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.youxing.common.utils.Log;
import com.youxing.sogoteacher.app.SGApplication;

public class PhotoPicker {
	private static final String LOG_TAG = PhotoPicker.class.getSimpleName();
	//
	// The launch code when picking a photo and the raw data is returned
	//
	public static final int REQUEST_CODE_PHOTO_PICKED = 3021;
	//
	// The launch code when taking a picture
	//
	public static final int REQUEST_CODE_CAMERA = 3022;

	private Context mContext = null;
	private String strImgPath;
	private Fragment fragment = null;

	public PhotoPicker(Context context) {
		this.mContext = context;
	}

	public PhotoPicker(Fragment frag) {
		this.fragment = frag;
	}

	/** call before the super.onSaveInstanceState */
	public void onSaveInstanceState(Bundle outState) {
		outState.putString("strImgPath", strImgPath);
	}

	/** call after the super.onRestoreInstanceState */
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		strImgPath = savedInstanceState.getString("strImgPath");
	}

	public void doTakePhoto() {
		try {
			if (mContext != null) {
				((Activity) mContext).startActivityForResult(
						getTakePhotoIntent(), REQUEST_CODE_CAMERA);
			} else if (fragment != null) {
				fragment.startActivityForResult(getTakePhotoIntent(),
						REQUEST_CODE_CAMERA);
			}

		} catch (ActivityNotFoundException e) {
			if (mContext != null) {
				Toast.makeText(mContext, "手机上没有照片。", Toast.LENGTH_LONG).show();
			} else if (fragment != null && fragment.getActivity() != null) {
				Toast.makeText(fragment.getActivity(), "手机上没有照片。",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public void doPickPhotoFromGallery() {
		try {
			Intent intent = getPhotoPickIntent();
			if (mContext != null) {
				((Activity) mContext).startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						REQUEST_CODE_PHOTO_PICKED);
			} else if (fragment != null) {
				fragment.startActivityForResult(
						Intent.createChooser(intent, "Select Picture"),
						REQUEST_CODE_PHOTO_PICKED);
			}

		} catch (Exception e) {
			e.printStackTrace();
			if (mContext != null) {
				Toast.makeText(mContext, "手机上没有照片。", Toast.LENGTH_LONG).show();
			} else if (fragment != null && fragment.getActivity() != null) {
				Toast.makeText(fragment.getActivity(), "手机上没有照片。",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	// should called after doTakePhoto()
	public void doCropPhoto() {
		if (strImgPath == null) {
			Log.e(LOG_TAG, "the file path of the croped photo is null");
			return;
		}

		try {
			File tempFile = new File(strImgPath);
			Uri uri = Uri.fromFile(tempFile);
			final Intent intent = getCropImageIntent(uri);
			if (mContext != null) {
				((Activity) mContext).startActivityForResult(intent,
						REQUEST_CODE_PHOTO_PICKED);
			} else if (fragment != null) {
				fragment.startActivityForResult(intent,
						REQUEST_CODE_PHOTO_PICKED);
			}

		} catch (Exception e) {

			if (mContext != null) {
				Toast.makeText(mContext, "手机上没有照片。", Toast.LENGTH_LONG).show();
			} else if (fragment != null && fragment.getActivity() != null) {
				Toast.makeText(fragment.getActivity(), "手机上没有照片。",
						Toast.LENGTH_LONG).show();
			}

		}
	}

	public String strImgPath() {
		return strImgPath;
	}

	public String parseImgPath(Intent data) {
		if (mContext == null) {
			return null;
		}
		String path = null;
		if (data != null) {
			Uri uri = data.getData();
			if (uri != null) {
				Cursor cursor = null;
				String[] proj = { MediaStore.Images.Media.DATA };
				if (mContext != null) {
					cursor = mContext.getContentResolver().query(uri, proj,
							null, null, null);
				} else if (fragment != null && fragment.getActivity() != null) {
					cursor = fragment.getActivity().getContentResolver()
							.query(uri, proj, null, null, null);
				}

				if (cursor == null)
					return null;

				try {
					cursor.moveToFirst();
					path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
				} catch (Exception e) {
					Toast.makeText(mContext, "请换一个文件夹试试！", Toast.LENGTH_SHORT)
							.show();
					e.printStackTrace();
				} finally {
					if (cursor != null) {
						cursor.close();
					}
				}
			}
		}
		return path;
	}

	protected File getOutFile() {
		String imgPath = Environment.getExternalStorageDirectory().toString()
				+ "/DCIM/Camera/";
		String fileName = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date()) + ".jpg";
		File out = new File(imgPath);
		if (!out.exists()) {
			out.mkdirs();
		}
		out = new File(imgPath, fileName);
		strImgPath = imgPath + fileName;
		return out;
	}

	protected Intent getTakePhotoIntent() {
		Uri uri = Uri.fromFile(getOutFile());
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		return intent;
	}

	protected Intent getPhotoPickIntent() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			Intent i = new Intent();
			i.setType("image/jpeg");
			i.setAction(Intent.ACTION_GET_CONTENT);
			intent = Intent.createChooser(i, "Select Picture");
		} else {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.addCategory(Intent.CATEGORY_OPENABLE);
			intent.setType("image/jpeg");
		}
		return intent;
	}

	protected Intent getCropImageIntent(Uri photoUri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX());
		intent.putExtra("outputY", outputY());
		intent.putExtra("return-data", true);
		return intent;
	}

	protected int outputX() {
		return 144;
	}

	protected int outputY() {
		return 144;
	}

	public Bitmap parseThumbnail(String file) {
		int sampling = 1;
		try {
			FileInputStream ins = new FileInputStream(file);
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(ins, null, opt);
			ins.close();

			int size = opt.outWidth > opt.outHeight ? opt.outWidth
					: opt.outHeight;
			if (size < 1400)
				sampling = 1;
			else if (size < 2800)
				sampling = 2;
			else
				sampling = 4;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		Bitmap showBitmap = null;

		for (; sampling <= 8 && showBitmap == null; sampling *= 2) {
			try {
				FileInputStream ins = new FileInputStream(file);
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inSampleSize = sampling;
				showBitmap = BitmapFactory.decodeStream(ins, null, opt);
				ins.close();
			} catch (OutOfMemoryError oom) {
				System.gc();
				Toast.makeText(SGApplication.instance(), "内存溢出",
						Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		return showBitmap;
	}

	public Bitmap parseThumbnail(Context ctx, Uri data) {
		int sampling = 1;
		try {
			InputStream ins = ctx.getContentResolver().openInputStream(data);
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(ins, null, opt);
			ins.close();

			int size = opt.outWidth > opt.outHeight ? opt.outWidth
					: opt.outHeight;
			if (size < 1400)
				sampling = 1;
			else if (size < 2800)
				sampling = 2;
			else
				sampling = 4;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		Bitmap showBitmap = null;

		for (; sampling <= 8 && showBitmap == null; sampling *= 2) {
			try {
				InputStream ins = ctx.getContentResolver().openInputStream(data);
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inSampleSize = sampling;
				showBitmap = BitmapFactory.decodeStream(ins, null, opt);
				ins.close();
			} catch (OutOfMemoryError oom) {
				System.gc();
				Toast.makeText(SGApplication.instance(), "内存溢出",
						Toast.LENGTH_LONG).show();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		return showBitmap;
	}

	/**
	 * Get a file path from a Uri. This will get the the path for Storage Access
	 * Framework Documents, as well as the _data field for the MediaStore and
	 * other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @author paulburke
	 */
	public static String getPath(final Context context, final Uri uri) {

		final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

		// DocumentProvider
		if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
			// ExternalStorageProvider
			if (isExternalStorageDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				if ("primary".equalsIgnoreCase(type)) {
					return Environment.getExternalStorageDirectory() + "/" + split[1];
				}

				// TODO handle non-primary volumes
			}
			// DownloadsProvider
			else if (isDownloadsDocument(uri)) {

				final String id = DocumentsContract.getDocumentId(uri);
				final Uri contentUri = ContentUris.withAppendedId(
						Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

				return getDataColumn(context, contentUri, null, null);
			}
			// MediaProvider
			else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];

				Uri contentUri = null;
				if ("image".equals(type)) {
					contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}

				final String selection = "_id=?";
				final String[] selectionArgs = new String[] {
						split[1]
				};

				return getDataColumn(context, contentUri, selection, selectionArgs);
			}
		}
		// MediaStore (and general)
		else if ("content".equalsIgnoreCase(uri.getScheme())) {
			return getDataColumn(context, uri, null, null);
		}
		// File
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}


	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}
}
