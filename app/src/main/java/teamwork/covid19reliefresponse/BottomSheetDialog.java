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
    private ArrayList<String> strings;
    private int row_index,lastPosition;
    private TextView questionTextView,nextTextView;
    private EditText inputEditText;
    private StorageReference storageRef;
    private DatabaseReference mRootRef;
    private DatabaseReference foodHamperRef, announcementRef;
    private FirebaseAuth mAuth;


    public ArrayList<String> getStrings() {

        // TODO go through this list and set questions and inputType for the edittext per question
        strings=new ArrayList<>();
        strings.add("Maina ka botlalo");
        strings.add("Le kahe mo lapeng");
        strings.add("Tsenya nomoro ya Omang ka botlalo");
        strings.add("Tsenya motse/toropo , kgotla le nomoro ya Jarata");
        strings.add("A go na le masea mo lapeng, fa a le teng a kafe");
        strings.add("A go na le dijo dingwe tse di sa jeweng mo lapeng, fa di le teng ke dife?");
        strings.add("Fa o abelela dijo tse di mo lapeng ka nako e di ka fela leng?");
        strings.add("Re kopa mogala o re ka go tshwarang mo go one");
        return strings;
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
            questionTextView.setText(getStrings().get(0));

                break;
            case 1:
                //begin form with the first question from the permit/gbv housing assistence set
                questionTextView.setText(getStrings().get(0));


                break;

        }

    }

    private  void setQuestion(){
        lastPosition=lastPosition+1;
        switch (lastPosition) {

            case 0:
                if (lastPosition < getStrings().size())
                    questionTextView.setText(getStrings().get(lastPosition));

                else
                    //the questionnaire has been completed put the Hamper object together and send it to Firebase
                //might need a switch or if else statement to put each answer in the right place in the Hamper object



                break;
            case 1:
               //to same for next type of questionnaire

                if (lastPosition < getStrings().size())
                    questionTextView.setText(getStrings().get(lastPosition));
                else
                    //the questionnaire has been completed put the Hamper object together and send it to Firebase
                //use the  foodHamperRef node
                break;

        }



    }
}
