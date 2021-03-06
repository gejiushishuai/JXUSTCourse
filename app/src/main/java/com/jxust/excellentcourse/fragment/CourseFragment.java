package com.jxust.excellentcourse.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.jxust.excellentcourse.R;
import com.jxust.excellentcourse.Utils.Utils;
import com.jxust.excellentcourse.activity.CourseDetailActivity;
import com.jxust.excellentcourse.activity.LoginActivity;
import com.jxust.excellentcourse.base.BaseFragment;
import com.panxw.android.imageindicator.ImageIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Simon
 * @created 2017/2/14
 */
public class CourseFragment extends BaseFragment {
    private ImageIndicatorView imageIndicatorView;
    private ListView listView;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_course,container,false);
        imageIndicatorView= (ImageIndicatorView) view.findViewById(R.id.indicate_view);
        Utils.loadImage(imageIndicatorView);
        listView= (ListView) view.findViewById(R.id.lv_fragment_study);
        loadListViewData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AVUser user=AVUser.getCurrentUser();
                String academy=user.get("academy").toString();
                AVQuery<AVObject> query = new AVQuery<>("Course");
                query.whereEqualTo("type",academy);
                query.orderByAscending("createdAt");
                query.findInBackground(new FindCallback<AVObject>() {
                    @Override
                    public void done(List<AVObject> list, AVException e) {
                        if (list!=null)
                        {
                            Intent intent=new Intent(mactivity,CourseDetailActivity.class);
                            AVObject course=list.get(position);
                            String viewPerson=course.get("viewperson").toString();
                            Log.i("<<<",viewPerson);
                            int viewcount=Integer.parseInt(viewPerson);
                            viewcount++;
                            course.put("viewperson",viewcount+"");
                            course.saveInBackground();
                            Log.i(">>>>",course.toString());
                            Bundle bundle=new Bundle();
                            bundle.putParcelable("course",course);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
        return view;
    }
    private void loadListViewData() {
        String academy;
        AVUser user=AVUser.getCurrentUser();
        if (user!=null)
        {
            academy=user.get("academy").toString();
            Utils.loadCourseFromAvo(listView,academy,mactivity);

        }
        else
        {
            startActivity(new Intent(mactivity,LoginActivity.class));
        }
    }
}
