package com.Client;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.assurance.*;
import com.assurance.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Add extends Activity {

	// Progress Dialog
	private ProgressDialog pDialog;

	JSONParser jsonParser = new JSONParser();
	EditText inputName;
	EditText inputAdress;
	EditText inputPhone;
	EditText inputPlate;
	EditText inputCar;
	EditText inputFees;

	// url to create new product
	private static String url_create_product = "http://smb215.hostyd.com/android_connect/create_product.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add);

		// Edit Text
		inputName = (EditText) findViewById(R.id.inputName);
		inputAdress = (EditText) findViewById(R.id.inputAdress);
		inputPhone = (EditText) findViewById(R.id.inputPhone);
		inputPlate = (EditText) findViewById(R.id.inputPlate);
		inputCar = (EditText) findViewById(R.id.inputCar);
		inputFees = (EditText) findViewById(R.id.inputFees);


		// Create button
		Button btnCreateProduct = (Button) findViewById(R.id.btnCreateProduct);

		// button click event
		btnCreateProduct.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// creating new product in background thread
				new CreateNewProduct().execute();
			}
		});
	}

	/**
	 * Background Async Task to Create new product
	 * */
	class CreateNewProduct extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Add.this);
			pDialog.setMessage("Saving Client..");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			String name = inputName.getText().toString();
			String adress = inputAdress.getText().toString();
			String phone = inputPhone.getText().toString();
			String plate = inputPlate.getText().toString();
			String car = inputCar.getText().toString();
			String fees = inputFees.getText().toString();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", name));
			params.add(new BasicNameValuePair("adress", adress));
			params.add(new BasicNameValuePair("phone", phone));
			params.add(new BasicNameValuePair("plate", plate));
			params.add(new BasicNameValuePair("car", car));
			params.add(new BasicNameValuePair("fees", fees));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_product,
					"POST", params);
			
			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					Intent i = new Intent(getApplicationContext(), DashboardActivity.class);
					startActivity(i);
					
					// closing this screen
					finish();
				} else {
					// failed to create product
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once done
			pDialog.dismiss();
		}

	}
}
