package com.sikefeng.tongxuelu.activity;


import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sikefeng.tongxuelu.R;
import com.sikefeng.tongxuelu.activity.fragment.ContactsFragment;
import com.sikefeng.tongxuelu.activity.user.User;
import com.sikefeng.tongxuelu.activity.user.UserUtils;
import com.sikefeng.tongxuelu.adapter.ImagesAdapter;
import com.sikefeng.tongxuelu.baseactivity.BaseActivity;
import com.sikefeng.tongxuelu.bmobutils.BmobFileUtils;
import com.sikefeng.tongxuelu.clipimage.ClipActivity;
import com.sikefeng.tongxuelu.dialog.DateDialog;
import com.sikefeng.tongxuelu.dialog.LoadingDialog;
import com.sikefeng.tongxuelu.diray.DataPersonImpl;
import com.sikefeng.tongxuelu.entity.Person;
import com.sikefeng.tongxuelu.multipleimages.ChildAdapter;
import com.sikefeng.tongxuelu.multipleimages.startSelectActivity;
import com.sikefeng.tongxuelu.widgets.MyGridView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DeleteBatchListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;


public class EditPersonActivity extends BaseActivity {
    private MyGridView mGridView;
    private ImagesAdapter adapter;
    private ArrayList<String> imageList = new ArrayList<String>();
    private ArrayList<String> pathList = null;
    private TextView tv_birthday;
    private EditText subjectEditText, nameEditText, ageEditText,
            addressEditText, phonoEditText, wordsEditText, hobbyEditText,
            qqEditText, emailEditText, constellationEditText, bloodEditText, nicknameEditText, dreamEditText,
            bestcolorEditText, beststarEditText, bestsportEditText;
    private RelativeLayout subject_layout;
    private String subject, flag, name, age, sex, address, phonoNumber, hoddy,
            qq, e_mail, constellation, birthday, booldType, nickname, words,
            dream, best_Color, best_Star, best_Sport, touxiang, images1, images2,
            images3, images4, images5, images6, images7, images8, images9;
    private String[] imagespath = new String[9];
    private RadioGroup radioGroup;
    private TextView add_picture;

    private ImageView head;
    private PopupWindow popWindow;
    private LayoutInflater layoutInflater;
    private TextView photograph, albums;
    private LinearLayout cancel;

    public static final int PHOTOZOOM = 0;
    public static final int PHOTOTAKE = 1;
    public static final int IMAGE_COMPLETE = 2;
    public static final int CROPREQCODE = 3;
    private String photoSavePath;
    private String photoSaveName;
    private String path;
    private String temppath;
    private boolean isNewImage = false;
    private LoadingDialog loadingDialog;
    private BmobFileUtils bmobFileUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register_layout);
        head = (ImageView) findViewById(R.id.headview);

        initView();// 锟斤拷始锟斤拷锟斤拷图
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        File file = new File(Environment.getExternalStorageDirectory(),
                "TongXueLu/HeadImages");
        if (!file.exists()) {
            file.mkdirs();
        }

        photoSavePath = Environment.getExternalStorageDirectory()
                + "/TongXueLu/HeadImages/";
        photoSaveName = "headview" + ".png";

        head.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                showPopupWindow(head);
            }
        });
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                EditPersonActivity.this.finish();
            }
        });
        bmobFileUtils = new BmobFileUtils(EditPersonActivity.this);
    }// **********************************************************************************************


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 111) {

            pathList = ChildAdapter.getArrayList();
            for (int i = 0; i < pathList.size(); i++) {
                imageList.add(pathList.get(i));
            }
            ArrayList<String> path = null;
            if (imageList.size() > 9) {
                path = new ArrayList<String>();
                for (int i = 0; i < 9; i++) {
                    path.add(imageList.get(i));
                }
                adapter = new ImagesAdapter(this, path, mGridView);
                mGridView.setAdapter(adapter);
                Toast.makeText(getApplicationContext(), "最多添加9张图片", Toast.LENGTH_SHORT).show();
            } else {
                adapter = new ImagesAdapter(this, imageList, mGridView);
                mGridView.setAdapter(adapter);
            }
        }
        if (resultCode != RESULT_OK) {
            return;
        }
        Uri uri = null;
        switch (requestCode) {
            case PHOTOZOOM:// 锟斤拷锟�
                if (data == null) {
                    return;
                }
                uri = data.getData();
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(uri, proj, null, null, null);
                int column_index = cursor
                        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(column_index);// 图片锟节碉拷路锟斤拷
                Intent intent3 = new Intent(this, ClipActivity.class);
                intent3.putExtra("path", path);
                startActivityForResult(intent3, IMAGE_COMPLETE);
                break;
            case PHOTOTAKE:// 锟斤拷锟斤拷
                path = photoSavePath + photoSaveName;
                uri = Uri.fromFile(new File(path));
                Intent intent2 = new Intent(this, ClipActivity.class);
                intent2.putExtra("path", path);
                startActivityForResult(intent2, IMAGE_COMPLETE);
                break;
            case IMAGE_COMPLETE:
                temppath = data.getStringExtra("path");
                head.setImageBitmap(getLoacalBitmap(temppath));
                if (temppath != null) {
                    isNewImage = true;
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @SuppressWarnings("deprecation")
    private void showPopupWindow(View parent) {
        if (popWindow == null) {
            View view = layoutInflater.inflate(R.layout.pop_select_photo, null);
            popWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT, true);
            initPop(view);
        }
        popWindow.setAnimationStyle(android.R.style.Animation_InputMethod);
        popWindow.setFocusable(true);
        popWindow.setOutsideTouchable(true);
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        popWindow
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    public void initPop(View view) {
        photograph = (TextView) view.findViewById(R.id.photograph);// 锟斤拷锟斤拷********
        albums = (TextView) view.findViewById(R.id.albums);// 锟斤拷锟� ********
        cancel = (LinearLayout) view.findViewById(R.id.cancel);// 取锟斤拷
        photograph.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                popWindow.dismiss();
                photoSaveName = "headview" + ".png";
                Uri imageUri = null;
                // 通锟斤拷锟斤拷锟秸伙拷取图片
                Intent openCameraIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                imageUri = Uri.fromFile(new File(photoSavePath, photoSaveName));
                openCameraIntent.putExtra(MediaStore.Images.Media.ORIENTATION,
                        0);
                openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(openCameraIntent, PHOTOTAKE);
            }
        });
        albums.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                popWindow.dismiss();
                // 通锟斤拷图锟斤拷锟饺⊥计�
                Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                openAlbumIntent
                        .setDataAndType(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                "image/*");
                startActivityForResult(openAlbumIntent, PHOTOZOOM);
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) {
                popWindow.dismiss();
            }
        });
    }


    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void initView() {
        final Button savebButton = (Button) findViewById(R.id.savamess);
        radioGroup = (RadioGroup) findViewById(R.id.SexradioGroup);
        add_picture = (TextView) findViewById(R.id.add_picture);
        subject_layout = (RelativeLayout) findViewById(R.id.subject_layout);
        mGridView = (MyGridView) findViewById(R.id.child_grid);
        subjectEditText = (EditText) findViewById(R.id.subject_edit);
        nameEditText = (EditText) findViewById(R.id.name_edit);
        ageEditText = (EditText) findViewById(R.id.age_edit);
        addressEditText = (EditText) findViewById(R.id.address_edit);
        phonoEditText = (EditText) findViewById(R.id.phono_edit);
        wordsEditText = (EditText) findViewById(R.id.motto_edit);
        hobbyEditText = (EditText) findViewById(R.id.hobby_edit);
        qqEditText = (EditText) findViewById(R.id.qq_edit);
        emailEditText = (EditText) findViewById(R.id.email_edit);
        constellationEditText = (EditText) findViewById(R.id.constallation_edit);
        tv_birthday = (TextView) findViewById(R.id.tv_birthday);
        bloodEditText = (EditText) findViewById(R.id.bloodtype_edit);
        nicknameEditText = (EditText) findViewById(R.id.nickname_edit);
        dreamEditText = (EditText) findViewById(R.id.dream_edit);
        bestcolorEditText = (EditText) findViewById(R.id.best_color_edit);
        beststarEditText = (EditText) findViewById(R.id.best_star_edit);
        bestsportEditText = (EditText) findViewById(R.id.best_sport_edit);
        tv_birthday.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DateDialog dialog = new DateDialog(EditPersonActivity.this);
                dialog.setDate(tv_birthday);
            }
        });
        Intent intent = getIntent();
        final int id = intent.getIntExtra("id", 0);
        final Person person = new DataPersonImpl(EditPersonActivity.this).findPerson(id);
        flag = person.getFlag();
        subject = person.getSubject();
        name = person.getName();
        sex = person.getSex();
        age = person.getAge();
        address = person.getAddress();
        phonoNumber = person.getPhonoNumber();
        words = person.getWords();
        hoddy = person.getHoddy();
        qq = person.getQQ();
        e_mail = person.getE_mail();
        constellation = person.getConstellation();
        birthday = person.getBirthday();
        booldType = person.getBooldType();
        nickname = person.getNickname();
        dream = person.getDream();
        best_Color = person.getBest_Color();
        best_Sport = person.getBest_sports();
        best_Star = person.getBest_star();
        touxiang = person.getTouxiang();
        if (person.getImages1() != null) {
            imageList.add(person.getImages1());
        }
        if (person.getImages2() != null) {
            imageList.add(person.getImages2());
        }
        if (person.getImages3() != null) {
            imageList.add(person.getImages3());
        }
        if (person.getImages4() != null) {
            imageList.add(person.getImages4());
        }
        if (person.getImages5() != null) {
            imageList.add(person.getImages5());
        }
        if (person.getImages6() != null) {
            imageList.add(person.getImages7());
        }
        if (person.getImages7() != null) {
            imageList.add(person.getImages7());
        }
        if (person.getImages8() != null) {
            imageList.add(person.getImages8());
        }
        if (person.getImages9() != null) {
            imageList.add(person.getImages9());
        }
        adapter = new ImagesAdapter(this, imageList, mGridView);
        mGridView.setAdapter(adapter);

        subjectEditText.setText(subject);
        nameEditText.setText(name);
        ageEditText.setText(age);
        addressEditText.setText(address);
        phonoEditText.setText(phonoNumber);
        wordsEditText.setText(words);
        hobbyEditText.setText(hoddy);
        qqEditText.setText(qq);
        emailEditText.setText(e_mail);
        constellationEditText.setText(constellation);
        tv_birthday.setText(birthday);
        nicknameEditText.setText(nickname);
        bloodEditText.setText(booldType);
        dreamEditText.setText(dream);
        bestcolorEditText.setText(best_Color);
        bestsportEditText.setText(best_Sport);
        beststarEditText.setText(best_Star);

        String icon_url=person.getTouxiang();
        if (TextUtils.isEmpty(icon_url)){
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(EditPersonActivity.this));
            ImageLoader.getInstance().displayImage("http://bmob-cdn-5625.b0.upaiyun.com/2016/08/15/f369c47b2162497faa83b7f92784ac17.jpg", head);
        }else{
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(EditPersonActivity.this));
            ImageLoader.getInstance().displayImage(icon_url, head);
        }

        temppath = "/sdcard/TongXueLu/HeadImages/" + name + ".png";

        add_picture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(EditPersonActivity.this, startSelectActivity.class);
                startActivityForResult(intent, 111);

            }
        });
        if (!"teacher".equals(flag)) {
            subject_layout.setVisibility(View.GONE);
        }
        savebButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (nameEditText.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "姓名不能为空!", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < radioGroup.getChildCount(); i++) {
                        RadioButton radioButton = (RadioButton) radioGroup
                                .getChildAt(i);
                        if (radioButton.isChecked()) {
                            sex = radioButton.getText().toString();
                        }
                    }
                    subject = subjectEditText.getText().toString().trim();
                    name = nameEditText.getText().toString().trim();
                    age = ageEditText.getText().toString().trim();
                    address = addressEditText.getText().toString().trim();
                    phonoNumber = phonoEditText.getText().toString().trim();
                    words = wordsEditText.getText().toString().trim();
                    hoddy = hobbyEditText.getText().toString().trim();
                    qq = qqEditText.getText().toString().trim();
                    e_mail = emailEditText.getText().toString().trim();
                    constellation = constellationEditText.getText().toString().trim();
                    birthday = tv_birthday.getText().toString().trim();
                    booldType = bloodEditText.getText().toString().trim();
                    nickname = nicknameEditText.getText().toString().trim();
                    dream = dreamEditText.getText().toString().trim();
                    best_Color = bestcolorEditText.getText().toString().trim();
                    best_Sport = bestsportEditText.getText().toString().trim();
                    best_Star = beststarEditText.getText().toString().trim();
                    if (imageList != null && imageList.size() > 0) {

                        if (imageList.size() > 9) {
                            for (int i = 0; i < 9; i++) {
                                imagespath[i] = imageList.get(i);
                            }
                        } else {
                            for (int i = 0; i < imageList.size(); i++) {
                                imagespath[i] = imageList.get(i);
                            }
                        }
                    }
                    if (isNewImage) {  //头像被修改
                        final BmobFile bmobFile = new BmobFile(new File(temppath));
                        bmobFile.uploadblock(EditPersonActivity.this, new UploadFileListener() {
                            @Override
                            public void onSuccess() {
                                //头像上传成功后，删除原来的头像文件
                                bmobFileUtils.deletefile(person.getTouxiang());
                                touxiang = bmobFile.getFileUrl(EditPersonActivity.this);
                                Person p = new Person(id, flag, subject, name, age, words,
                                        sex, phonoNumber, address, hoddy, qq, e_mail,
                                        constellation, birthday, booldType, nickname,
                                        dream, best_Color, best_Sport, best_Star, touxiang, imagespath[0],
                                        imagespath[1], imagespath[2], imagespath[3], imagespath[4], imagespath[5],
                                        imagespath[6], imagespath[7], imagespath[8]);
                                new DataPersonImpl(EditPersonActivity.this).updatePerson(p);
                                updateUserMsg();
                            }

                            @Override
                            public void onStart() {
                                super.onStart();
                                loadingDialog = LoadingDialog.getInstance();
                                loadingDialog.showDialogForLoading(EditPersonActivity.this, true, "正在修改数据......");
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                loadingDialog.hideDialogForLoading();
                                savebButton.setEnabled(true);
                            }
                        });
                    } else {
                        loadingDialog = LoadingDialog.getInstance();
                        loadingDialog.showDialogForLoading(EditPersonActivity.this, true, "正在修改数据...");
                        Person p = new Person(id, flag, subject, name, age, words,
                                sex, phonoNumber, address, hoddy, qq, e_mail,
                                constellation, birthday, booldType, nickname,
                                dream, best_Color, best_Sport, best_Star, touxiang, imagespath[0],
                                imagespath[1], imagespath[2], imagespath[3], imagespath[4], imagespath[5],
                                imagespath[6], imagespath[7], imagespath[8]);
                        new DataPersonImpl(EditPersonActivity.this).updatePerson(p);
                        updateUserMsg();

                    }

                }


            }
        });

    }

    private void updateUserMsg() {
        User loginUser = new UserUtils(EditPersonActivity.this).getLoginUser();
        Gson gson = new Gson();
        List<Person> personList = new DataPersonImpl(EditPersonActivity.this).findAllPersons();
        loginUser.setPersonJsonData(gson.toJson(personList));
        loginUser.update(EditPersonActivity.this, new UpdateListener() {
            @Override
            public void onSuccess() {
                loadingDialog.hideDialogForLoading();
                ContactsFragment.isReLoad = true; //FriendsActivity2重新加载数据
                finish();
                Toast.makeText(getApplicationContext(), "修改成功!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int i, String s) {
                loadingDialog.hideDialogForLoading();
                ContactsFragment.isReLoad = true; //FriendsActivity2重新加载数据
                finish();

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    public void deletefile(String url) {
        //此url必须是上传文件成功之后通过bmobFile.getUrl()方法获取的。
        String[] urls = new String[]{url};
        BmobFile.deleteBatch(EditPersonActivity.this, urls, new DeleteBatchListener() {
            @Override
            public void done(String[] strings, BmobException e) {
                if (e == null) {
//					toast("全部删除成功");
                } else {
                    if (strings != null) {
//						toast("删除失败个数：" + strings.length + "," + e.toString());
                    } else {
//						toast("全部文件删除失败：" + e.getErrorCode() + "," + e.toString());
                    }
                }
            }
        });
    }

}
