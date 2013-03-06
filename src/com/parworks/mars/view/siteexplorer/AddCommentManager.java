package com.parworks.mars.view.siteexplorer;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.parworks.androidlibrary.ar.ARErrorListener;
import com.parworks.androidlibrary.ar.ARListener;
import com.parworks.androidlibrary.ar.ARSite;
import com.parworks.androidlibrary.ar.ARSites;
import com.parworks.mars.model.sync.SyncHandler;
import com.parworks.mars.utils.User;

public class AddCommentManager implements android.view.View.OnClickListener {

	public static final String TAG = AddCommentManager.class.getName();

	private final Activity mActivity;
	private final String mSiteId;

	public AddCommentManager(Activity activity, String siteId) {
		mActivity = activity;
		mSiteId = siteId;
	}

	@Override
	public void onClick(View view) {
		Session.openActiveSession(mActivity, true,
				new Session.StatusCallback() {

					// callback when session changes state
					@Override
					public void call(Session session, SessionState state,
							Exception exception) {
						Log.d(TAG, "call");
						if (session.isOpened()) {

							// make request to the /me API
							Request.executeMeRequestAsync(session,
									new Request.GraphUserCallback() {

										// callback after Graph API response
										// with user object
										@Override
										public void onCompleted(GraphUser user,
												Response response) {
											Log.d(TAG, "onCompleted");
											if (user != null) {
												mActivity
														.startActivity(new Intent(
																mActivity,
																LeaveCommentActivity.class)
																.putExtra(
																		"userName",
																		user.getName())
																.putExtra(
																		"userId",
																		user.getId())
																.putExtra(
																		"siteId",
																		mSiteId));
											} else {
												Log.e(TAG, "User was null.");
											}
										}
									});
						} else {
							if (exception != null) {
								Toast.makeText(mActivity,
										"Failed to login to facebook.",
										Toast.LENGTH_SHORT).show();
								Log.d(TAG,
										"CALL EXCEPTION WAS: "
												+ exception.getMessage());
								Log.d(TAG,
										"APP ID: " + session.getApplicationId());
								Log.e(TAG, "session not opened.");
								Log.e(TAG, session.getState() + "");
								Log.e(TAG, session.toString());
							}
						}
					}
				});

	}

	public void facebookOnActivityResult(int requestCode, int resultCode,
			Intent data) {
		Session.getActiveSession().onActivityResult(mActivity, requestCode,
				resultCode, data);
	}

	public void addComment(String userId, String userName, String commentText) {
		storeComment(userId, userName, commentText);
	}

	private void storeComment(final String userId, final String userName,
			final String comment) {
		ARSites sites = User.getARSites();
		sites.getExisting(mSiteId, new ARListener<ARSite>() {
			@Override
			public void handleResponse(ARSite resp) {
				resp.addComment(userId, userName, comment,
						new ARListener<Void>() {
							@Override
							public void handleResponse(Void resp) {
								SyncHandler.syncSiteComments(mSiteId);
								Toast.makeText(mActivity, "Comment added!",
										Toast.LENGTH_SHORT).show();
							}
						}, new ARErrorListener() {
							@Override
							public void handleError(Exception error) {
								Log.e(TAG, error.getMessage());
							}
						});
			}
		}, new ARErrorListener() {
			@Override
			public void handleError(Exception error) {
				Log.e(TAG, error.getMessage());

			}
		});
	}
}
