package teamwork.covid19reliefresponse;

import android.os.Bundle;
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

public class BottomSheetDialog extends BottomSheetDialogFragment {
    private ArrayList<String> hamperQuestions,housingQuestions;
    private int row_index,lastPosition;
    private TextView questionTextView,nextTextView;
    private EditText inputEditText;
    private StorageReference storageRef;
    private DatabaseReference mRootRef;
    private DatabaseReference foodHamperRef, announcementRef;
    private FirebaseAuth mAuth;


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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        storageRef = FirebaseStorage.getInstance().getReference();

        mRootRef = FirebaseDatabase.getInstance().getReference();
        foodHamperRef = mRootRef.child("HamperRequests");
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

               setQuestion();
           }
       });

        return view;
    }

    public void getUI(int position) {
    }

    public void startQuestionnaire( int position){

        lastPosition=0;


      questionTextView.setVisibility(View.VISIBLE);
        inputEditText.setVisibility(View.VISIBLE);
        nextTextView.setVisibility(View.VISIBLE);

        //positions here are passed when opening the bottomSheet
        switch (position) {

            case 0:

        //begin form with the first question from the food hamper set
            questionTextView.setText(getHamperQuestions().get(0));

                break;
            case 1:
                //begin form with the first question from the permit/gbv housing assistence set
                questionTextView.setText(getHousingQuestions().get(0));


                break;

        }

    }

    private  void setQuestion(){
        lastPosition=lastPosition+1;
        switch (lastPosition) {

            case 0:
                if (lastPosition < getHamperQuestions().size())
                    questionTextView.setText(getHamperQuestions().get(lastPosition));

                else
                    //the questionnaire has been completed put the Hamper object together and send it to Firebase
                //might need a switch or if else statement to put each answer in the right place in the Hamper object



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
}
