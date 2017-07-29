package com.sikefeng.tongxuelu.sortlistview;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.sikefeng.tongxuelu.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SortListViewActivity extends Activity {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortAdapter adapter;

	

	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;
	
	private TextView tv_back;
	private PinyinComparator pinyinComparator;
	private List<String> phonesList;
	private List<String> name_phoneList;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sort_layout);
		phonesList=new ArrayList<String>();
		name_phoneList=new ArrayList<String>();
	
		initPhonoNumber();
		initViews();
		
		
	}

	private void initViews() {

		characterParser = CharacterParser.getInstance();
		
		pinyinComparator = new PinyinComparator();
		tv_back=(TextView)findViewById(R.id.tv_back);
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		tv_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//�����Ҳഥ������
		sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
			
			@Override
			public void onTouchingLetterChanged(String s) {
				//����ĸ�״γ��ֵ�λ��
				int position = adapter.getPositionForSection(s.charAt(0));
				if(position != -1){
					sortListView.setSelection(position);
				}
				
			}
		});
		
		sortListView = (ListView) findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String msg=((SortModel)adapter.getItem(position)).getName();
				String phoneNumber=msg.substring(msg.indexOf(":")+1, msg.length());
//				mClearEditText.setText(phoneNumber);
				if (!TextUtils.isEmpty(phoneNumber)){
					Intent intent=new Intent();
					intent.putExtra("phone",phoneNumber);
					setResult(115,intent);
					SortListViewActivity.this.finish();
				}else {
					SortListViewActivity.this.finish();
				}
			}
		});
		
		SourceDateList = filledData(phonesList);
		
		// ����a-z��������Դ����
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortAdapter(this, SourceDateList);
		sortListView.setAdapter(adapter);
		
		

	}



	private List<SortModel> filledData(List<String> list){
		List<SortModel> mSortList = new ArrayList<SortModel>();
		
		for(int i=0; i<list.size(); i++){
			SortModel sortModel = new SortModel();
			sortModel.setName(list.get(i));
			//����ת����ƴ��
			String pinyin = characterParser.getSelling(list.get(i));
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// ������ʽ���ж�����ĸ�Ƿ���Ӣ����ĸ
			if(sortString.matches("[A-Z]")){
				sortModel.setSortLetters(sortString.toUpperCase());
			}else{
				sortModel.setSortLetters("#");
			}
			
			mSortList.add(sortModel);
		}
		return mSortList;
		
	}
	
	/**
	 * ����������е�ֵ���������ݲ�����ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		List<SortModel> filterDateList = new ArrayList<SortModel>();
		
		if(TextUtils.isEmpty(filterStr)){
			filterDateList = SourceDateList;
		}else{
			filterDateList.clear();
			for(SortModel sortModel : SourceDateList){
				String name = sortModel.getName();
				if(name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())){
					filterDateList.add(sortModel);
				}
			}
		}
		
		// ����a-z��������
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}
	
	private void initPhonoNumber() {
		
		ContentResolver resolver = getContentResolver();
		Cursor cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,null);
		while(cursor.moveToNext()){
			int idIndex=cursor.getColumnIndex(ContactsContract.Contacts._ID);
			int nameIndex=cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			int id=cursor.getInt(idIndex);
			String contactName=cursor.getString(nameIndex);			
			name_phoneList.add(contactName);//�����ϵ������
			Cursor pCursor = resolver.query(Phone.CONTENT_URI, null,Phone.CONTACT_ID+"="+id, null,null);
			while (pCursor.moveToNext()) {
				int numberIndex = pCursor.getColumnIndex(Phone.NUMBER);
				String phonoNumber = pCursor.getString(numberIndex);				
				phonesList.add(contactName+":"+phonoNumber);
			}
		}
		cursor.close();

	}
	
	
}




