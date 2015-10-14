package com.doublechen.androidtest.ui;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import com.doublechen.androidtest.R;
import com.doublechen.androidtest.base.BaseActivity;
import com.doublechen.androidtest.database.EntityManager;
import com.doublechen.androidtest.database.EntityManagerFactory;
import com.doublechen.androidtest.database.entity.Staff;
import com.doublechen.androidtest.util.Logger;

public class DatabaseFragment extends Fragment implements View.OnClickListener {
    ExecutorService mExecutorService;
    EntityManagerFactory mEntityManagerFactory;

    private Button mButton;
    private TextView mTextView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String s = (String) msg.obj;
            mTextView.setText(s);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_database, container, false);

        mButton = (Button) rootView.findViewById(R.id.button);
        mButton.setOnClickListener(this);
        mTextView = (TextView) rootView.findViewById(R.id.textView);

        // Bundle args = getArguments();
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 做一些数据库相关工作
        mEntityManagerFactory = ((BaseActivity) getActivity()).app.getEntityManagerFactory();

        mExecutorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 2; i++) {
            mExecutorService.submit(new insertData(i));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                queryDatabase();
                break;
        }
    }

    private void queryDatabase() {
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                EntityManagerFactory factory = ((BaseActivity) getActivity()).app.getEntityManagerFactory();
                EntityManager em = factory.createEntityManager();
                List<Staff> staffList = (List<Staff>) em.query(Staff.class);
                StringBuilder sb = new StringBuilder();
                for (Staff staff : staffList) {
                    sb.append("id: ").append(staff.id).append(", name: ").append(staff.name).append("\n");
                }

                Message msg = Message.obtain();
                msg.obj = sb.toString();
                mHandler.sendMessage(msg);
            }
        });
    }

    class insertData implements Runnable {
        int id;

        public insertData(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            EntityManagerFactory factory = ((BaseActivity) getActivity()).app.getEntityManagerFactory();
            EntityManager em = factory.createEntityManager();
            /* insert staff 1 */
            Staff staff1 = new Staff();
            staff1.id = id * 2;
            staff1.name = "chen" + id;
            em.insertOrReplace(staff1, true);
            /* sleep, to force another DBHelper do its job */
            try {
                Thread.sleep(500);
            } catch (Exception e) {

            }
            /* insert staff 2 */
            Staff staff2 = new Staff();
            staff2.id = id * 2 + 1;
            staff2.name = "double" + id;
            em.insertOrReplace(staff2, true);

            Logger.d("insert finish", true);
        }
    }


}
