package nl.joriswit.soko.builtinlevelsolutions;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class SolutionActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        Intent intent = getIntent();
        // Since this app only has one intent-filter, the following should always be true.
        if(intent.getAction().equals("nl.joriswit.sokosolver.SOLVE")) {

            // Soko++ supplies the level as a "LEVEL" parameter in the intent.
            // The level is in XSB format.
            String level = intent.getStringExtra("LEVEL");

            // Get the SHA-1 digest from the level.
            MessageDigest sha1;
            try {
                sha1 = MessageDigest.getInstance("SHA1");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            byte[] digest = sha1.digest(level.getBytes());

            String solution;
            try {
                // Open the solutions file to search it. Note: it is considered a
                // bad practice in Android to do file I/O in the UI thread, but
                // for simplicity we do it anyway.
                InputStream raw = getResources().openRawResource(R.raw.solutions);
                solution = getSolution(raw, digest);
                raw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            TextView status = (TextView)this.findViewById(R.id.status);
            if (solution != null) {

                // This passes the solution back to Soko++.
                Intent resultIntent = new Intent();
                resultIntent.putExtra("SOLUTION", solution);
                setResult(RESULT_OK, resultIntent);

                status.setText(R.string.solution_found_text);
            } else {
                status.setText(R.string.solution_not_found_text);
            }

        }

        this.findViewById(R.id.close_button).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SolutionActivity.this.finish();
            }
        });
    }

    private static String getSolution(InputStream is, byte[] searcheddigest) throws IOException {

        DataInputStream in = new DataInputStream(new BufferedInputStream(is));

        String solution = null;
        byte[] sha1 = new byte[20];

        // File format is a repetition of 20 bytes sha1, each followed by the solution.
        // The solution is a UDLR string prefixed by a 2 byte string length.

        int bytesread;
        do {
            bytesread = in.read(sha1, 0, 20);
            if (bytesread == 20) {
                String udlr = in.readUTF();

                // Check if the sha1 digest from the file is the same
                // as the digest of the searched level
                if(Arrays.equals(sha1, searcheddigest)) {
                    // Solution is found!
                    solution = udlr;
                    break;
                }
            }
        } while (bytesread == 20);
        in.close();
        return solution;
    }
}
