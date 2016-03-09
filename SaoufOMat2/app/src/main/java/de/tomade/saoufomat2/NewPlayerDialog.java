package de.tomade.saoufomat2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.NumberPicker;

public class NewPlayerDialog extends Activity {
EditText etxtName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_player_dialog);
        this.etxtName = (EditText)findViewById(R.id.etxtName);

    }

    public String getName(){
        return this.etxtName.getText().toString();
    }
}
