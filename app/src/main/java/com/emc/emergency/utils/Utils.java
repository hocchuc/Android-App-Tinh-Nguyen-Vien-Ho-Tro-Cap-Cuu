package com.emc.emergency.utils;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class Utils {
	public static ProgressDialog dialogModel = null;

	public static void  showDialog(Context context, String title, String message, Boolean cancel) {

		if(dialogModel == null) {
			dialogModel = new ProgressDialog(context);
			dialogModel.setIndeterminate(true);
			dialogModel.setCancelable(cancel);
			//dialogModel.setContentView();

		} else {
			dialogModel.dismiss();
		}
		dialogModel.setTitle(title);
		dialogModel.setMessage(message);
		dialogModel.show();
	}

	/**
	 * Tắt dialog
	 */
	public static void closeDialog() {
		if(dialogModel != null) {
			dialogModel.dismiss();
		}
		dialogModel = null;
	}
	public static void ThongBao(Context context,String title, String message) {
		try {
			Dialog foundNothingDlg = new AlertDialog.Builder(context)
					.setIcon(0)
					.setTitle(title)
					.setPositiveButton("OK", null)
					.setMessage(message)
					.create();
			foundNothingDlg.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static double getDistance(double lat1 ,double lon1 ,double lat2, double lon2)
	{
		try
		{
			if (lat1 == 0 || lon1 == 0 || lat2 == 0 || lon2 == 0)
			{
				return 99999;
			}
			Location loc1 = new Location(LocationManager.GPS_PROVIDER);
			Location loc2 = new Location(LocationManager.GPS_PROVIDER);
			loc1.setLatitude(lat1);
			loc1.setLongitude(lon1);
			loc1.setAltitude(0);

			loc2.setLatitude(lat2);
			loc2.setLongitude(lon2);
			loc2.setAltitude(0);

			double distance = loc1.distanceTo(loc2);
			loc1 = null;
			loc2 = null;
			Log.d("Model","getDistance =" + distance);
			return distance;
		}
		catch (Exception e)
		{
			Log.d("Model","getDistance =" + e.getMessage());
			e.printStackTrace();
			return 99999;
		}
	}
	public static void showToast(Context context,String mess)
	{
		try {
			Toast.makeText(context, mess, Toast.LENGTH_LONG).show();
		} catch ( Exception e){}
	}
	public static void showToastShort(Context context,String mess)
	{
		try {
			Toast.makeText(context, mess, Toast.LENGTH_SHORT).show();
		} catch ( Exception e){}
	}

	public static double parseDouble(String s)
	{
		double value = 0;
		try{value = Double.parseDouble(s);}catch (Exception e){}
		return value;
	}
	public static int parseInt(String s)
	{
		int value = 0;
		try{value = Integer.parseInt(s);}catch (Exception e){}
		return value;
	}
	public static List<Address> getAddressFromLocation(Context context, double latitude, double longitude)
	{
		try {
			Geocoder geocoder;
			List<Address> addresses;
			geocoder = new Geocoder(context, Locale.getDefault());
			addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
			String fullAddress = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
			String city = addresses.get(0).getAdminArea();
			String country = addresses.get(0).getCountryName();
			String StreetName = addresses.get(0).getThoroughfare();
			String sonha = addresses.get(0).getSubThoroughfare();
			String phuong = addresses.get(0).getSubLocality(); // Only if available else return NULL
			String quan = addresses.get(0).getSubAdminArea();

			return addresses;//""+ address +", " +phuong +", "+quan +", "+city+", "+country;
		}catch (Exception e)
		{
			return null;
		}
	}
}
