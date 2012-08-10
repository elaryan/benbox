package com.xoninja.benbox;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import android.content.Context;

public class FileIO {
	
	private static final int MODE_WORLD_WRITEABLE = 2;

	void writeFileToInternalStorage(Context ctx, String titulo, String contenido) {
		String eol = System.getProperty("line.separator");
		BufferedWriter writer = null;
		try {
		  writer = new BufferedWriter(new OutputStreamWriter(ctx.openFileOutput(titulo + ".txt", MODE_WORLD_WRITEABLE)));
		  writer.write(contenido + eol);
		} catch (Exception e) {
				e.printStackTrace();
		} finally {
		  if (writer != null) {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }
		}
	} 

}
