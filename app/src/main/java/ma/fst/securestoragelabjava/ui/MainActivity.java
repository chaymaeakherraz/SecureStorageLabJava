package ma.fst.securestoragelabjava.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ma.fst.securestoragelabjava.R;
import ma.fst.securestoragelabjava.cache.CacheStore;
import ma.fst.securestoragelabjava.external.ExternalAppFilesStore;
import ma.fst.securestoragelabjava.files.InternalTextStore;
import ma.fst.securestoragelabjava.files.StudentsJsonStore;
import ma.fst.securestoragelabjava.model.Student;
import ma.fst.securestoragelabjava.prefs.AppPrefs;
import ma.fst.securestoragelabjava.prefs.SecurePrefs;

public class MainActivity extends AppCompatActivity {

    EditText edtName, edtLang, edtTheme, edtToken;
    TextView txtResult;

    Button btnSavePrefs, btnLoadPrefs;
    Button btnSaveToken, btnLoadToken;
    Button btnInternal, btnJson;
    Button btnCache, btnExternal;
    Button btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtName = findViewById(R.id.edtName);
        edtLang = findViewById(R.id.edtLang);
        edtTheme = findViewById(R.id.edtTheme);
        edtToken = findViewById(R.id.edtToken);

        txtResult = findViewById(R.id.txtResult);

        btnSavePrefs = findViewById(R.id.btnSavePrefs);
        btnLoadPrefs = findViewById(R.id.btnLoadPrefs);
        btnSaveToken = findViewById(R.id.btnSaveToken);
        btnLoadToken = findViewById(R.id.btnLoadToken);
        btnInternal = findViewById(R.id.btnInternal);
        btnJson = findViewById(R.id.btnJson);
        btnCache = findViewById(R.id.btnCache);
        btnExternal = findViewById(R.id.btnExternal);
        btnClear = findViewById(R.id.btnClear);

        btnSavePrefs.setOnClickListener(v -> savePrefs());

        btnLoadPrefs.setOnClickListener(v -> loadPrefs());

        btnSaveToken.setOnClickListener(v -> saveToken());

        btnLoadToken.setOnClickListener(v -> loadToken());

        btnInternal.setOnClickListener(v -> testInternal());

        btnJson.setOnClickListener(v -> testJson());

        btnCache.setOnClickListener(v -> testCache());

        btnExternal.setOnClickListener(v -> testExternal());

        btnClear.setOnClickListener(v -> clearAll());
    }

    private void savePrefs() {

        String name = edtName.getText().toString();

        String lang = edtLang.getText().toString();

        String theme = edtTheme.getText().toString();

        boolean ok = AppPrefs.save(
                this,
                name,
                lang,
                theme,
                false
        );

        txtResult.setText("SharedPreferences sauvegardées : " + ok);
    }

    private void loadPrefs() {

        AppPrefs.Triple data = AppPrefs.load(this);

        txtResult.setText(
                "Nom : " + data.name +
                        "\nLang : " + data.lang +
                        "\nTheme : " + data.theme
        );
    }

    private void saveToken() {

        try {

            String token = edtToken.getText().toString();

            SecurePrefs.saveToken(this, token);

            txtResult.setText(
                    "Token sauvegardé longueur : "
                            + token.length()
            );

        } catch (Exception e) {

            txtResult.setText(e.getMessage());
        }
    }

    private void loadToken() {

        try {

            String token = SecurePrefs.loadToken(this);

            txtResult.setText(
                    "Token récupéré longueur : "
                            + token.length()
            );

        } catch (Exception e) {

            txtResult.setText(e.getMessage());
        }
    }

    private void testInternal() {

        try {

            InternalTextStore.writeUtf8(
                    this,
                    "note.txt",
                    "Bonjour stockage interne"
            );

            String text = InternalTextStore.readUtf8(
                    this,
                    "note.txt"
            );

            txtResult.setText(text);

        } catch (Exception e) {

            txtResult.setText(e.getMessage());
        }
    }

    private void testJson() {

        try {

            List<Student> students = new ArrayList<>();

            students.add(new Student(1, "Chaimae", 21));
            students.add(new Student(2, "Sara", 22));
            students.add(new Student(3, "Youssef", 23));

            StudentsJsonStore.save(this, students);

            List<Student> loaded =
                    StudentsJsonStore.load(this);

            StringBuilder sb = new StringBuilder();

            for (Student s : loaded) {

                sb.append(s.id)
                        .append(" ")
                        .append(s.name)
                        .append(" ")
                        .append(s.age)
                        .append("\n");
            }

            txtResult.setText(sb.toString());

        } catch (Exception e) {

            txtResult.setText(e.getMessage());
        }
    }

    private void testCache() {

        try {

            CacheStore.write(
                    this,
                    "cache.txt",
                    "Temporary cache"
            );

            String read = CacheStore.read(
                    this,
                    "cache.txt"
            );

            int deleted = CacheStore.purge(this);

            txtResult.setText(
                    "Cache : "
                            + read
                            + "\nDeleted : "
                            + deleted
            );

        } catch (Exception e) {

            txtResult.setText(e.getMessage());
        }
    }

    private void testExternal() {

        try {

            String path =
                    ExternalAppFilesStore.write(
                            this,
                            "export.txt",
                            "External storage file"
                    );

            String read =
                    ExternalAppFilesStore.read(
                            this,
                            "export.txt"
                    );

            txtResult.setText(
                    "Path : \n"
                            + path
                            + "\n\nContent : \n"
                            + read
            );

        } catch (Exception e) {

            txtResult.setText(e.getMessage());
        }
    }

    private void clearAll() {

        try {

            AppPrefs.clear(this);

            SecurePrefs.clear(this);

            InternalTextStore.delete(
                    this,
                    "note.txt"
            );

            StudentsJsonStore.delete(this);

            CacheStore.purge(this);

            ExternalAppFilesStore.delete(
                    this,
                    "export.txt"
            );

            txtResult.setText(
                    "Toutes les données supprimées"
            );

        } catch (Exception e) {

            txtResult.setText(e.getMessage());
        }
    }
}