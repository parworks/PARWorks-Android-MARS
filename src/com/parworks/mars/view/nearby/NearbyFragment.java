package com.parworks.mars.view.nearby;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parworks.androidlibrary.ar.AugmentedData;
import com.parworks.androidlibrary.ar.Overlay;
import com.parworks.androidlibrary.ar.OverlayImpl;
import com.parworks.androidlibrary.ar.Vertex;
import com.parworks.androidlibrary.response.AugmentImageResultResponse;
import com.parworks.androidlibrary.response.OverlayAugmentResponse;
import com.parworks.arviewer.ARViewerActivity;
import com.parworks.mars.R;
import com.parworks.mars.cache.BitmapCache;
import com.parworks.mars.cache.BitmapWorkerTask;
import com.parworks.mars.cache.BitmapWorkerTask.BitmapWorkerListener;
import com.parworks.mars.model.db.AugmentedImagesTable;
import com.parworks.mars.model.provider.MarsContentProvider;
import com.parworks.mars.utils.JsonMapper;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class NearbyFragment extends Fragment {

	private SlidingFragmentActivity mContext;
	
	public NearbyFragment(SlidingFragmentActivity context) {
		super();
		mContext = context;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_nearby, null);
		
		testARViewer("63edf9b6-2657-4e46-91ad-490508d36a0e");
		return v;
	}
	
	private void testARViewer(final String imageId) {
		
		Cursor cursor = this.getActivity().getContentResolver().query(
				MarsContentProvider.getAugmentedImageUri(imageId), null, null, null, null);

		final String augmentedData = cursor.getString(cursor.getColumnIndex(AugmentedImagesTable.COLUMN_CONTENT));
		final String siteId = cursor.getString(cursor.getColumnIndex(AugmentedImagesTable.COLUMN_SITE_ID));
		final String contentUrl = cursor.getString(cursor.getColumnIndex(AugmentedImagesTable.COLUMN_CONTENT_SIZE_URL));
		final int width = cursor.getInt(cursor.getColumnIndex(AugmentedImagesTable.COLUMN_WIDTH));
		final int height = cursor.getInt(cursor.getColumnIndex(AugmentedImagesTable.COLUMN_HIEGHT));
		final Intent intent = new Intent(this.getActivity(), ARViewerActivity.class);
		intent.putExtra("site-id", siteId);
		intent.putExtra("image-id", imageId);
		
		Bitmap bitmap = BitmapCache.get().getBitmap(BitmapCache.getImageKeyFromURL(contentUrl));
		
			new BitmapWorkerTask(contentUrl, new BitmapWorkerListener() {				
				@Override
				public void bitmapLoaded(Bitmap bitmap) {
					if (bitmap != null) {
						try {
							String filename = Environment.getExternalStorageDirectory()
									.getAbsolutePath() + "/" + "parworks/test.jpeg";
						    FileOutputStream out = new FileOutputStream(filename);
						    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
						    System.out.println(filename);
						    intent.putExtra("file-path", filename);
						} catch (Exception e) {
						       e.printStackTrace();
						}
					}		
					
					AugmentImageResultResponse data;
					try {
						System.out.println("TEST: " + augmentedData);
						data = JsonMapper.get().readValue(augmentedData, AugmentImageResultResponse.class);
						intent.putExtra("augmented-data", convertAugmentResultResponse(imageId, data));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					intent.putExtra("original-size", width + "x" + height);
					
					startActivity(intent);
				}
			}).execute();
		
		
		
	}
	
	private AugmentedData convertAugmentResultResponse(String imgId,
			AugmentImageResultResponse result) {
		List<OverlayAugmentResponse> overlayResponses = result.getOverlays();
		List<Overlay> overlays = new ArrayList<Overlay>();

		for (OverlayAugmentResponse overlayResponse : overlayResponses) {
			overlays.add(makeOverlay(overlayResponse, imgId));
		}

		AugmentedData augmentedData = new AugmentedData(result.getFov(),
				result.getFocalLength(), result.getScore(),
				result.isLocalization(), overlays);
		return augmentedData;
	}
	private Overlay makeOverlay(OverlayAugmentResponse overlayResponse,
			String imgId) {
		Overlay overlay = new OverlayImpl(imgId, overlayResponse.getName(),
				overlayResponse.getDescription(),
				parseVertices(overlayResponse.getVertices()));
		return overlay;
	}
	private List<Vertex> parseVertices(String serverOutput) {
		String[] points = serverOutput.split(",");

		List<Vertex> vertices = new ArrayList<Vertex>();
		for (int i = 0; i < points.length; i += 3) {
			float xCoord = Float.parseFloat(points[i]);
			float yCoord = Float.parseFloat(points[i + 1]);
			float zCoord = Float.parseFloat(points[i + 2]);
			vertices.add(new Vertex(xCoord, yCoord, zCoord));
		}
		return vertices;
	}
	private void saveImage(byte[] data, String imageFilename)
			throws IOException {
		saveImage(new ByteArrayInputStream(data), imageFilename);
	}
	
	private void saveImage(InputStream data, String imageFilename)
			throws IOException {
		String storagePath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" + "parworks";

		// Make a directory, if not exists
		File f = new File(storagePath);
		if (!f.exists())
			f.mkdirs();

		// Write image file
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(imageFilename, false);
			IOUtils.copy(data, fos);
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
	}
}
