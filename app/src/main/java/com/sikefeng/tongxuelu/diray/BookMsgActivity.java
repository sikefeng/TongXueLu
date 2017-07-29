package com.sikefeng.tongxuelu.diray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.sikefeng.tongxuelu.R;


public class BookMsgActivity extends Activity {
    private TextView tv_title, tv_content,tv_back;
    private Intent intent=null;
    private Book book;
    public static boolean isReload=false;
    private DataImpl data;
    private int Id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_book_msg);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_back=(TextView)findViewById(R.id.tv_back);
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        data=new DataImpl(BookMsgActivity.this);
        initData();
    }
   private void initData(){
       intent=this.getIntent();
       book=data.findBook(intent.getIntExtra("id",0));
       Id=book.getId();
       tv_title.setText(book.getTitle());
       tv_content.setText(book.getContent());

   }
    public void onEditClick(View view) {
        Intent intent=new Intent(BookMsgActivity.this, UpdateBookActivity.class);
        intent.putExtra("id",Id);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReload){
            Book book=(Book)data.findBook(Id);
            tv_title.setText(book.getTitle());
            tv_content.setText(book.getContent());
            isReload=false;
        }
    }

}
