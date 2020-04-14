package teamwork.covid19reliefresponse;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import teamwork.covid19reliefresponse.model.HamperRequest;
import teamwork.covid19reliefresponse.model.QuestionModel;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private ArrayList<String> housingQuestions,donationQuestions,hamperQuestions;
  //  private ArrayList<QuestionModel>hamperQuestions;
    private int row_index,lastPosition,testType;
    private TextView questionTextView,nextTextView;
    private EditText inputEditText;
    private StorageReference storageRef;
    private DatabaseReference mRootRef;
    private DatabaseReference foodHamperRef, announcementRef,housingRef,donationRef;
    private FirebaseAuth mAuth;
    private  HamperRequest hamperRequest;

// trying new idea on compiling answers
    /*
    public ArrayList<QuestionModel> getHamperQuestions() {
//String name, String id, String phoneNumber, String location, Integer people, Integer infants, String allergies, String lastMealDate
        // TODO go through this list and set questions and inputType for the edittext per question
        hamperQuestions=new ArrayList<>();
        hamperQuestions.add(new QuestionModel("String","setName","Maina ka botlalo")));
        hamperQuestions.add(new QuestionModel("String","setId","Le kahe mo lapeng"));
        hamperQuestions.add(new QuestionModel("String","setPhoneNumber","Tsenya nomoro ya Omang ka botlalo"));
        hamperQuestions.add(new QuestionModel("String","setLocation","Tsenya motse/toropo , kgotla le nomoro ya Jarata"));
        hamperQuestions.add(new QuestionModel("Integer","setPeople","A go na le masea mo lapeng, fa a le teng a kafe"));
        hamperQuestions.add(new QuestionModel("Integer","setInfants","A go na le dijo dingwe tse di sa jeweng mo lapeng, fa di le teng ke dife?"));
        hamperQuestions.add(new QuestionModel("String","setAllergies","Fa o abelela dijo tse di mo lapeng ka nako e di ka fela leng?"));
        hamperQuestions.add(new QuestionModel("String","setLastMealDate","Re kopa mogala o re ka go tshwarang mo go one"));
        return hamperQuestions;
    }
    */


    public ArrayList<String> getHamperQuestions() {

        // TODO go through this list and set questions and inputType for the edittext per question
        hamperQuestions=new ArrayList<>();
        hamperQuestions.add("Maina ka botlalo");
        hamperQuestions.add("Le kahe mo lapeng");
        hamperQuestions.add("Tsenya nomoro ya Omang ka botlalo");
        hamperQuestions.add("Tsenya motse/toropo , kgotla le nomoro ya Jarata");
        hamperQuestions.add("A go na le masea mo lapeng, fa a le teng a kafe");
        hamperQuestions.add("A go na le dijo dingwe tse di sa jeweng mo lapeng, fa di le teng ke dife?");
        hamperQuestions.add("Fa o abelela dijo tse di mo lapeng ka nako e di ka fela leng?");
        hamperQuestions.add("Re kopa mogala o re ka go tshwarang mo go one");
        return hamperQuestions;
    }

    public ArrayList<String> getHousingQuestions() {
        // TODO go through this list and set questions and inputType for the edittext per question
        housingQuestions=new ArrayList<>();
        housingQuestions.add("Maina ka botlalo");
        housingQuestions.add("Tsenya nomoro ya Omang ka botlalo");
        housingQuestions.add("Tsenya motse/toropo , kgotla le nomoro ya Jarata");
        housingQuestions.add("Re kopa mogala o re ka go tshwarang mo go one");
        housingQuestions.add("Re kopa mogala wa mongwe yo o mo tshephang, yo re ka go tshwarang mo go one");
        return housingQuestions;
    }

    public ArrayList<String> getDonationQuestions() {
        // TODO go through this list and set questions and inputType for the edittext per question
       donationQuestions=new ArrayList<>();

        donationQuestions.add("O batla go aba eng le eng?");
        donationQuestions.add("Tsenya motse/toropo , kgotla le nomoro ya Jarata");
        donationQuestions.add("Re kopa mogala o re ka go tshwarang mo go one");
        donationQuestions.add("Re ka tla go tsaya dilo tse nako mang?,Kwala A fa nako tsotlhe di siame");
        return  donationQuestions;
    }
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        storageRef = FirebaseStorage.getInstance().getReference();

        mRootRef = FirebaseDatabase.getInstance().getReference();
        foodHamperRef = mRootRef.child("HamperRequests");
        housingRef = mRootRef.child("HousingRequests");
       donationRef = mRootRef.child("DonationList");
        foodHamperRef.keepSynced(true);
        announcementRef = mRootRef.child("Announcements");
        //bottom sheet round corners can be obtained but the while background appears to remove that we need to add this.
        setStyle(DialogFragment.STYLE_NO_FRAME,0);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        inputEditText=view.findViewById(R.id.input_edittext);
        questionTextView=view.findViewById(R.id.question_textview);
       nextTextView=view.findViewById(R.id.next_text);


       nextTextView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //set next question on the list

               compileAnswers();
               setQuestion();
           }
       });

        return view;
    }

    public void getUI(int position) {
    }

    public void startQuestionnaire( int position){

        lastPosition=0;
        testType=position;

      questionTextView.setVisibility(View.VISIBLE);
        inputEditText.setVisibility(View.VISIBLE);
        nextTextView.setVisibility(View.VISIBLE);

        //positions here are passed when opening the bottomSheet
        switch (position) {

            case 0:

        //begin form with the first question from the food hamper set
            questionTextView.setText(getHamperQuestions().get(0));

              hamperRequest=new HamperRequest();

                break;
            case 1:
                //begin form with the first question from the permit/gbv housing assistence set
                questionTextView.setText(getHousingQuestions().get(0));


                break;

        }

    }

    private  void setQuestion(){
        lastPosition=lastPosition+1;
        switch (testType) {

            case 0:
                //TODO
                if (lastPosition < getHamperQuestions().size()) {
                    //get the question from the current position and set it
                    questionTextView.setText(getHamperQuestions().get(lastPosition));

                    if (lastPosition == 2 || lastPosition == 4){
                        //make provision to get only numbers as answers for these two questions
                        questionTextView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                }
                    else if (lastPosition == 7){
                        //make provision to get only numbers as answers for this questions
                        questionTextView.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
                }
                    else{
                        //make provision to get only test as answers for these other questions
                        questionTextView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                }}
                else {
                    //the questionnaire has been completed put the Hamper object together and send it to Firebase
                    //might need a switch or if else statement to put each answer in the right place in the Hamper object


                }

                break;
            case 1:
               //to same for next type of questionnaire

                if (lastPosition < getHousingQuestions().size())
                    questionTextView.setText(getHousingQuestions().get(lastPosition));
                else
                    //the questionnaire has been completed put the Hamper object together and send it to Firebase
                //use the  foodHamperRef node
                break;

        }



    }

    private  void compileAnswers(){

        if(testType==0) {

//How do we know what entry we are setting to HamperRequest.




        }else if(testType==1){



        }else{


        }
    }
}
