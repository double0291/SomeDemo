package com.doublechen.androidtest.ui;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.doublechen.androidtest.R;
import com.doublechen.androidtest.base.BaseActivity;
import com.doublechen.androidtest.database.EntityManager;
import com.doublechen.androidtest.database.EntityManagerFactory;
import com.doublechen.androidtest.database.entity.Staff;

public class TwoFragment extends Fragment {
	ExecutorService mExecutorService;
	EntityManagerFactory mEntityManagerFactory;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_two, container, false);

		// Bundle args = getArguments();
		return rootView;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 做一些数据库相关工作
		mEntityManagerFactory = ((BaseActivity) getActivity()).app.getEntityManagerFactory();

		mExecutorService = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 100; i++) {
			mExecutorService.submit(new insertData(i));
		}
	}

	class insertData implements Runnable {
		int id;

		public insertData(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			// EntityManagerFactory factory = ((BaseActivity) getActivity()).app.getEntityManagerFactory();
			EntityManager em = mEntityManagerFactory.createEntityManager();
			/* insert staff 1 */
			Staff staff1 = new Staff();
			staff1.id = id * 2;
			staff1.name = "chen" + id;
			em.insert(staff1);
			/* sleep, to force another DBHelper do its job */
			try {
				Thread.sleep(500);
			} catch (Exception e) {

			}
			/* insert staff 2 */
			Staff staff2 = new Staff();
			staff2.id = id * 2 + 1;
			staff2.name = "double" + id;
			em.insert(staff2);
		}
	}
}
