package com.saas.chat.net;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.juns.health.net.loopj.android.http.JsonHttpResponseHandler;
import com.saas.chat.Constants;
import com.saas.chat.common.Utils;
import com.small.saasuser.application.MyApplication;

public abstract class BaseJsonRes extends JsonHttpResponseHandler {

	@Override
	public void onSuccess(JSONObject response) {
		try {
			String result = response.getString(Constants.Result);
			// System.out.println("返回的值" + response);
			if (result == null) {
				Utils.showLongToast(MyApplication.getmContext(), Constants.NET_ERROR);
			} else if (result.equals("1")) {
				String str_data = response.getString(Constants.Value);
				onMySuccess(str_data);
			} else {
				String str = response.getString(Constants.Info);
				Utils.showLongToast(MyApplication.getmContext(), str);
				onMyFailure();
			}
		} catch (JSONException e) {
			e.printStackTrace();
			onMyFailure();
		}
	}

	public abstract void onMySuccess(String data);

	public abstract void onMyFailure();
}
