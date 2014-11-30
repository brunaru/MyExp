package br.usp.myexp.ui;

import android.app.Activity;
import android.os.Bundle;
import br.usp.myexp.QuestionnaireManager;
import br.usp.myexp.R;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        QuestionnaireManager manager = new QuestionnaireManager();
        manager.scheduleAlarms(getApplicationContext());
    }
    
}
