package br.usp.myexp.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import br.usp.myexp.Constants;
import br.usp.myexp.QuestionnaireManager;
import br.usp.myexp.R;
import br.usp.myexp.Util;
import br.usp.myexp.ems.json.Answer;
import br.usp.myexp.ems.json.AnswersGroup;
import br.usp.myexp.ems.xml.MultipleChoice;
import br.usp.myexp.ems.xml.OpenText;
import br.usp.myexp.ems.xml.Questionnnaire;
import br.usp.myexp.log.Logger;
import br.usp.myexp.log.QuestionnaireEvent;

public class QuestionnaireActivity extends Activity {
    
    private String questionnaireId;
    final private Logger logger = new Logger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String fileName = getIntent().getStringExtra(Constants.QUESTIONNAIRE_FILE);
        createQuestionnaire(fileName);
    }

    @SuppressLint("InflateParams")
    private void createQuestionnaire(String fileName) {

        final QuestionnaireManager manager = new QuestionnaireManager();
        final Questionnnaire questionnnaire = manager.readQuestionnaire(fileName);
        questionnaireId = questionnnaire.getId();
        
        final AnswersGroup answersGroup = new AnswersGroup();
        final List<Answer> answers = new ArrayList<Answer>();
        Date date = new Date();
        long dateMs = date.getTime();
        answersGroup.setDateMs(dateMs);
        answersGroup.setAnswers(answers);

        List<MultipleChoice> multiqs = questionnnaire.getQuestions().getMultipleChoiceQuestions();
        List<OpenText> openqs = questionnnaire.getQuestions().getOpenTextQuestions();

        /*
         * Positions map: key is the question number, value is the position in
         * its respective list. The map is created to order all the questions
         * positions.
         */
        Map<Integer, String> positions = new TreeMap<Integer, String>();
        for (int i = 0; i < multiqs.size(); i++) {
            String pos = "M" + i;
            positions.put(multiqs.get(i).getNumber(), pos);
        }
        for (int i = 0; i < openqs.size(); i++) {
            String pos = "O" + i;
            positions.put(openqs.get(i).getNumber(), pos);
        }

        /* List of layouts. */
        final List<RelativeLayout> layouts = new ArrayList<RelativeLayout>();
        /* Total number of questions. */
        final int numberQuestions = multiqs.size() + openqs.size();
        /* Creates a layout for each question. */
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        for (int k = 1; k <= numberQuestions; k++) {

            final RelativeLayout layout;
            final String number;
            final String text;
            final int questionNumber = k;
            final boolean obligatory;

            String pos = positions.get(k);
            if (pos.contains("M")) {
                /* Multiple choice question */
                int realPos = Integer.valueOf(pos.substring(1));
                MultipleChoice question = multiqs.get(realPos);
                number = String.valueOf(question.getNumber());
                obligatory = question.getObligatory();
                if (obligatory) {
                    text = question.getText() + "*";
                } else {
                    text = question.getText();
                }

                if (question.getOnlyone() == true) {
                    /* RADIO. */
                    layout = (RelativeLayout) inflater.inflate(R.layout.question_multi_onlyone, null);
                    RadioGroup radioGroupMulti = (RadioGroup) layout.findViewById(R.id.radioGroupMulti);
                    List<String> options = question.getOptions();
                    for (final String option : options) {
                        final RadioButton radio = new RadioButton(getApplicationContext());
                        radio.setText(option);
                        radio.setTextAppearance(this, R.style.QText);
                        radio.setPadding(0, 5, 0, 10);
                        int colorId = Resources.getSystem().getIdentifier("btn_radio_holo_light", "drawable", "android");
                        radio.setButtonDrawable(colorId);
                        radioGroupMulti.addView(radio);
                        
                        /* Logger */
                        radio.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                QuestionnaireEvent event = new QuestionnaireEvent(questionnaireId, Util.getDate(),
                                        Logger.RADIO_CLICK_EVENT, number, option);
                                logger.logQuestionnaireEvent(event);
                            }
                        });
                        
                    }
                } else {
                    /* CHECKBOX. */
                    layout = (RelativeLayout) inflater.inflate(R.layout.question_multi_check, null);
                    LinearLayout linearCheck = (LinearLayout) layout.findViewById(R.id.linearLayCheckBox);
                    List<String> options = question.getOptions();
                    for (final String option : options) {
                        CheckBox check = new CheckBox(getApplicationContext());
                        check.setText(option);
                        check.setTextAppearance(this, R.style.QText);
                        check.setPadding(0, 5, 0, 10);
                        getResources();
                        int colorId = Resources.getSystem().getIdentifier("btn_check_holo_light", "drawable", "android");
                        check.setButtonDrawable(colorId);
                        linearCheck.addView(check);
                        
                        /* Logger */
                        check.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                QuestionnaireEvent event = new QuestionnaireEvent(questionnaireId, Util.getDate(),
                                        Logger.CHECKBOX_CLICK_EVENT, number, option);
                                logger.logQuestionnaireEvent(event);
                            }
                        });
                        
                    }
                }

            } else { // pos.contains("O")
                /* Open text question */
                int realPos = Integer.valueOf(pos.substring(1));
                OpenText question = openqs.get(realPos);
                number = String.valueOf(question.getNumber());                
                obligatory = question.getObligatory();
                if (obligatory) {
                    text = question.getText() + "*";
                } else {
                    text = question.getText();
                }
                layout = (RelativeLayout) inflater.inflate(R.layout.question_opentext, null);
                EditText editTextOpen = (EditText) layout.findViewById(R.id.editTextOpen);
                /* Logger */
                editTextOpen.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QuestionnaireEvent event = new QuestionnaireEvent(questionnaireId, Util.getDate(),
                                Logger.EDIT_TEXT_CLICK_EVENT, number, "editText");
                        logger.logQuestionnaireEvent(event);
                    }
                });
            }
            
            /* Logger */
            layout.setOnClickListener(new OnClickListener() {                
                @Override
                public void onClick(View v) {
                    QuestionnaireEvent event = new QuestionnaireEvent(questionnaireId, Util.getDate(), Logger.LAYOUT_CLICK_EVENT, number);            
                    logger.logQuestionnaireEvent(event);
                }
            });
            View scrollView = layout.findViewById(R.id.scrollView);            
            if (scrollView != null) {
                ScrollView scrollViewReal = (ScrollView) scrollView;
                scrollViewReal.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QuestionnaireEvent event = new QuestionnaireEvent(questionnaireId, Util.getDate(), Logger.LAYOUT_CLICK_EVENT, number);
                        logger.logQuestionnaireEvent(event);
                    }
                });
            }
            
            /* Add layout to list. */
            layouts.add(layout);
            
            
            /* COMMON text and number fields. */
            TextView textViewNumber = (TextView) layout.findViewById(R.id.textViewNumber);
            TextView textViewQuestion = (TextView) layout.findViewById(R.id.textViewQuestion);
            textViewNumber.setText(number + ".");
            textViewQuestion.setText(text);
            /* Logger */
            textViewQuestion.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    QuestionnaireEvent event = new QuestionnaireEvent(questionnaireId, Util.getDate(),
                            Logger.TEXT_CLICK_EVENT, number, text);
                    logger.logQuestionnaireEvent(event);
                }
            });

            /* BUTTONS. */
            final Button buttonConfirm = (Button) layout.findViewById(R.id.buttonConfirm);
            final Button buttonBack = (Button) layout.findViewById(R.id.buttonBack);
            
            if (questionNumber == numberQuestions) {
                /* Last question. */
                buttonConfirm.setText(R.string.finish);
            }
            /* Button Confirm. */
            buttonConfirm.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                    EditText editTextOpen = (EditText) layout.findViewById(R.id.editTextOpen);
                    String answerText = "";
                    Boolean obligatory = true;
                    if (editTextOpen != null) {
                        /* Text. */
                        answerText = editTextOpen.getText().toString();
                    } else {
                        RadioGroup radioGroupMulti = (RadioGroup) layout.findViewById(R.id.radioGroupMulti);
                        if (radioGroupMulti != null) {
                            /* Radio. */
                            RadioButton radio = (RadioButton) findViewById(radioGroupMulti.getCheckedRadioButtonId());
                            if (radio != null) {
                                answerText = radio.getText().toString();
                            }
                        } else {
                            /* Checkbox. */
                            LinearLayout linearCheck = (LinearLayout) layout.findViewById(R.id.linearLayCheckBox);
                            int n = linearCheck.getChildCount();
                            for (int c = 0; c < n; c++) {
                                CheckBox checkBox = (CheckBox) linearCheck.getChildAt(c);
                                if (checkBox.isChecked()) {
                                    if (answerText.isEmpty()) {
                                        answerText = checkBox.getText().toString();
                                    } else {
                                        answerText = answerText + "-" + checkBox.getText().toString();
                                    }
                                }
                            }
                        }
                    }

                    if (answerText == null) {
                        answerText = "";
                    }

                    if (!answerText.isEmpty() || obligatory == false) {
                        final Answer answer = new Answer(questionNumber, answerText);
                        answers.add(answer);
                        if (questionNumber == numberQuestions) {
                            /* Last question. */
                            AlertDialog dialog = confirmFinishDialog(manager, answersGroup, questionnnaire.getId(), number);
                            dialog.show();
                        } else {
                            /* Next. */
                            setContentView(layouts.get(questionNumber));
                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.error_answer_question, Toast.LENGTH_LONG);
                        LinearLayout toastLayout = (LinearLayout) toast.getView();
                        TextView toastTV = (TextView) toastLayout.getChildAt(0);
                        toastTV.setTextSize(22);
                        toastTV.setAlpha(1);
                        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                        toast.show();
                    }

                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(buttonConfirm.getWindowToken(), 0);
                    
                    /* Logger */
                    QuestionnaireEvent event = new QuestionnaireEvent(questionnaireId, Util.getDate(),
                                    Logger.BUTTON_CLICK_EVENT, number, "buttonConfirm", answerText);
                    logger.logQuestionnaireEvent(event);
                }
            });
            
            /* Button Back. */
            if (questionNumber == 1) {
                /* First question. */
                buttonBack.setVisibility(View.INVISIBLE);
            }
            buttonBack.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.playSoundEffect(android.view.SoundEffectConstants.CLICK);
                    answers.remove(answers.size() - 1);
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(buttonBack.getWindowToken(), 0);
                    setContentView(layouts.get(questionNumber - 2));
                    /* Logger */
                    QuestionnaireEvent event = new QuestionnaireEvent(questionnaireId, Util.getDate(),
                                    Logger.BUTTON_CLICK_EVENT, number, "buttonBack");
                    logger.logQuestionnaireEvent(event);
                }
            });
        }

        /* First screen. */
        setContentView(layouts.get(0));

    }

    private AlertDialog confirmFinishDialog(final QuestionnaireManager manager, final AnswersGroup answersGroup,
            final String questionnaireId, final String number) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_finish).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                manager.writeQuestionAnswers(answersGroup, questionnaireId, getApplicationContext());
                /* Logger */
                QuestionnaireEvent event = new QuestionnaireEvent(questionnaireId, Util.getDate(), 
                        Logger.DIALOG_CLICK_EVENT, number, getResources().getString(R.string.yes));
                logger.logQuestionnaireEvent(event);
                finish();
                System.exit(0);
            }
        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                answersGroup.getAnswers().remove(answersGroup.getAnswers().size() - 1);
                /* Logger */
                QuestionnaireEvent event = new QuestionnaireEvent(questionnaireId, Util.getDate(), 
                        Logger.DIALOG_CLICK_EVENT, number, getResources().getString(R.string.no));
                logger.logQuestionnaireEvent(event);
                dialog.cancel();
            }
        });
        return builder.create();
    }
    
    private AlertDialog confirmExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_exit).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                /* Logger */
                QuestionnaireEvent event = new QuestionnaireEvent(questionnaireId, Util.getDate(), 
                        Logger.DIALOG_CLICK_EVENT, "1", getResources().getString(R.string.yes));
                logger.logQuestionnaireEvent(event);                
                finish();
                System.exit(0);
            }
        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                /* Logger */
                QuestionnaireEvent event = new QuestionnaireEvent(questionnaireId, Util.getDate(), 
                        Logger.DIALOG_CLICK_EVENT, "1", getResources().getString(R.string.no));
                logger.logQuestionnaireEvent(event);                
                dialog.cancel();
            }
        });
        return builder.create();
    }

    @Override
    public void onBackPressed() {
        
        /* Logger */
        TextView textViewNumber = (TextView) findViewById(R.id.textViewNumber);
        QuestionnaireEvent event = new QuestionnaireEvent(questionnaireId, Util.getDate(), Logger.BACK_CLICK_EVENT, textViewNumber.getText().toString());            
        logger.logQuestionnaireEvent(event);  
        
        View buttonBackView = findViewById(R.id.buttonBack);
        if (buttonBackView != null && buttonBackView.isShown()) { 
            /* Back Question */           
            Button buttonBack = (Button) buttonBackView;            
            buttonBack.performClick();
        } else { /* Quit app */            
            AlertDialog dialog = confirmExitDialog();
            dialog.show();
        }
    }

}
