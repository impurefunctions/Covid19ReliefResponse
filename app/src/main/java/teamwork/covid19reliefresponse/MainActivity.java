package teamwork.covid19reliefresponse;

import android.content.Context;
import android.os.Bundle;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import teamwork.covid19reliefresponse.data.RecyclerViewAdapter;
import teamwork.covid19reliefresponse.model.Announcement;
import teamwork.covid19reliefresponse.model.HamperRequest;
import teamwork.covid19reliefresponse.model.HousingRequest;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> strings;
    private BottomSheetDialog bottomSheetDialog1;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LinearLayoutManager layoutManager, requestsLayoutManager,servicesLayoutManager ;
    private   FirebaseRecyclerAdapter<Announcement, Viewholder>mAdapter;
    private   FirebaseRecyclerAdapter<HamperRequest, RequestsViewholder>hamperAdapter;
    private   FirebaseRecyclerAdapter<HousingRequest, HousingViewholder>housingAdapter;
    private RecyclerView mRecyclerView, requestsRecyclerView,servicesRecyclerView;
    private StorageReference storageRef;
    private DatabaseReference mRootRef;
    private DatabaseReference foodHamperRef, announcementRef,housingRef,acceptedHamperRef,acceptedhousingRef,rejectedHamperRef,rejectedhousingRef;
    private Context context;
    private int swipedPosition;
    private FirebaseAuth mAuth;

    public ArrayList<String> getStrings() {

        strings = new ArrayList<>();
        strings.add("Food packages");
        strings.add("Request DMV help");
        strings.add("Permits");
        strings.add("Emergency contacts");

        return strings;
    }
    private void getUserPicture() {
/*
        RequestOptions requestOptions;
        requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        // Get the profile photo's url
        if (mUser != null) {

            for (UserInfo profile : mUser.getProviderData()) {
                if (profile.getProviderId().equals("facebook.com")) {

                    String facebookUserId = profile.getUid();

                    String providerId= profile.getProviderId();
                    // construct the URL to the profile picture, with a custom height
                    // alternatively, use '?type=small|medium|large' instead of ?height=

                    String   photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?height=500";

                    GlideApp
                            .with(context)
                            .load(photoUrl)
                            .apply(requestOptions)
                            .into(userImage);

                }else if (profile.getProviderId().equals("google.com")) {

                    Uri photoUrl = profile.getPhotoUrl();


                    // Variable holding the original String portion of the url that will be replaced
                    String originalPieceOfUrl = "s96-c/photo.jpg";

                    // Variable holding the new String portion of the url that does the replacing, to improve image quality
                    String newPieceOfUrlToAdd = "s400-c/photo.jpg";



                    // Check if the Url path is null

                    if (photoUrl != null) {

                        // Convert the Url to a String and store into a variable
                        String photoPath = photoUrl.toString();

                        // Replace the original part of the Url with the new part
                        String newString = photoPath.replace(originalPieceOfUrl, newPieceOfUrlToAdd);

                        // Load user's profile photo from their signed-in provider into the image view (with newly edited Url for resolution improvement)
                        // because file name is always same
                        //.skipMemoryCache(true);

                        GlideApp
                                .with(context)
                                .load(newString)
                                .apply(requestOptions)
                                .into(userImage);

                    }
                    Log.i("WHO BE YOU PROVIDER ?", mUser.getProviderId());

                    // if(profile.getProviderId().equals())
                    //    'https://graph.facebook.com/v2.12/me?fields=name,picture.width(800).height(800),first_name,last_name,email&access_token=${fbToken}');
                }

            }
        }

 */
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = MainActivity.this;
        mAuth = FirebaseAuth.getInstance();

        storageRef = FirebaseStorage.getInstance().getReference();

        mRootRef = FirebaseDatabase.getInstance().getReference();

        foodHamperRef = mRootRef.child("HamperRequests");
        housingRef = mRootRef.child("HousingRequests");

        rejectedHamperRef = mRootRef.child("RejectedHamperRequests");
        rejectedhousingRef = mRootRef.child("rejectedHousingRequests");
       acceptedHamperRef = mRootRef.child("AcceptedHamperRequests");
        acceptedhousingRef = mRootRef.child("AcceptedHousingRequests");

        foodHamperRef.keepSynced(true);
        announcementRef = mRootRef.child("Announcements");


        mRecyclerView = (RecyclerView) findViewById(R.id.announcement_recycler);
        servicesRecyclerView = (RecyclerView) findViewById(R.id.service_recycler);
       requestsRecyclerView = (RecyclerView) findViewById(R.id.requests_recycler);

        layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);

        servicesLayoutManager= new LinearLayoutManager(context);
       servicesRecyclerView.setLayoutManager(servicesLayoutManager);

        requestsLayoutManager = new LinearLayoutManager(context);
       requestsRecyclerView.setLayoutManager( requestsLayoutManager);

        RecyclerViewAdapter.RecyclerViewClickListener listener = (View view, int position) -> {

            // Pass position 0 for Food packages
            // Pass position 1 for Request DMV help
            // Pass position 2 for  Permits
            // Pass position 3 for Emergency contacts

            bottomSheetDialog1 = new BottomSheetDialog();
            bottomSheetDialog1.startQuestionnaire(position);;
            bottomSheetDialog1.show(getSupportFragmentManager(), "bottomsheet");
        };


        recyclerViewAdapter = new RecyclerViewAdapter(listener, getStrings());
        //bookAdapter=new DiscoverAdapter(books);

        servicesRecyclerView.setAdapter(recyclerViewAdapter);

        getAnnouncements();
        getHousingRequests();
        getHamperRequests();
        //TODO call next line after checking if user is a hamper delivery volunteer
        getHamperRequests();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getHousingRequests() {
        FirebaseRecyclerOptions<HousingRequest> options =
                new FirebaseRecyclerOptions.Builder<HousingRequest>()
                        .setQuery(housingRef, HousingRequest.class)
                        .build();

        housingAdapter = new FirebaseRecyclerAdapter<HousingRequest,HousingViewholder>(options) {

            @Override
            protected void onBindViewHolder(HousingViewholder holder, int position,HousingRequest model) {

                holder.name.setText( String.valueOf(model.getName()));
                holder.location.setText( String.valueOf(model.getLocation()));
                holder.children.setText( String.valueOf(model.getChildren()));
                holder.emergencyNumber.setText( String.valueOf(model.getEmergencyContact()));

                holder.phoneNumber.setText( String.valueOf(model.getPhoneNumber()));


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        String contentId= hamperAdapter.getRef(position).getKey();

                        return true;
                    }
                });

            }
            @Override
            public HousingViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.housing_layout, parent, false);
                final HousingViewholder vh = new HousingViewholder(view);
                context = parent.getContext();

                vh.setOnClickListener(new HousingViewholder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {



                    }
                    @Override
                    public void onItemLongClick(View view, int position) {


                        //    Toast.makeText(getActivity(), "Item long clicked at " + position, Toast.LENGTH_SHORT).show();
                    }



                });
                return vh;
            }





        };
/*
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mAdapter.getItemCount();
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    layoutManager.scrollToPosition(positionStart);
                }
            }
        });
*/
        //  mAdapter.startListening();
        // mRecyclerView.setLayoutManager(layoutManager);
        requestsRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();

    }

    public static class HousingViewholder extends RecyclerView.ViewHolder {


        public TextView name,id,location,children,phoneNumber,emergencyNumber;


        public HousingViewholder(View v) {
            super(v);

            name=(TextView) v.findViewById(R.id.beneficiary_name);
            id=(TextView) v.findViewById(R.id.title);
            location=(TextView) v.findViewById(R.id.location);
             phoneNumber=(TextView) v.findViewById(R.id.title);
            children=(TextView) v.findViewById(R.id.infants);
            emergencyNumber=(TextView) v.findViewById(R.id.beneficiaries_number);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickListener.onItemLongClick(v, getAdapterPosition());
                    return true;
                }
            });

        }


        private HousingViewholder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener {
            public void onItemClick(View view, int position);

            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(HousingViewholder.ClickListener clickListener) {
            mClickListener = clickListener;
        }


    }


    private void getAnnouncements() {

        FirebaseRecyclerOptions<Announcement> options =
                new FirebaseRecyclerOptions.Builder<Announcement>()
                        .setQuery(announcementRef, Announcement.class)
                        .build();

         mAdapter = new FirebaseRecyclerAdapter<Announcement, Viewholder>(options) {

            @Override
            protected void onBindViewHolder(Viewholder holder, int position, Announcement model) {

                holder.title.setText(String.valueOf(model.getTitle()));
                holder.timeStamp.setText(String.valueOf(model.getPublishedAt()));


                StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(model.getImageUrl());

/*
                RequestOptions requestOptions;  requestOptions = new RequestOptions()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC); // because file name is always same
                .skipMemoryCache(true);
                GlideApp
                        .with(context)
                        .load(ref)
                        .apply(requestOptions)
                        .into(holder.image);
               */

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return true;
                    }
                });

            }

            @Override
            public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.article_layout, parent, false);
                final Viewholder vh = new Viewholder(view);
                context = parent.getContext();

                vh.setOnClickListener(new Viewholder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                    }

                    @Override
                    public void onItemLongClick(View view, int position) {


                        //    Toast.makeText(getActivity(), "Item long clicked at " + position, Toast.LENGTH_SHORT).show();
                    }


                });
                return vh;
            }


        };
/*
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mAdapter.getItemCount();
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    layoutManager.scrollToPosition(positionStart);
                }
            }
        });
*/
        //  mAdapter.startListening();
        // mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }


    public static class Viewholder extends RecyclerView.ViewHolder {


        public TextView title, timeStamp;
        public ImageView image;

        public Viewholder(View v) {
            super(v);

            image = (ImageView) v.findViewById(R.id.article_image);
            title = (TextView) v.findViewById(R.id.title);
            timeStamp = (TextView) v.findViewById(R.id.published_at);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickListener.onItemLongClick(v, getAdapterPosition());
                    return true;
                }
            });

        }


        private Viewholder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener {
            public void onItemClick(View view, int position);

            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(Viewholder.ClickListener clickListener) {
            mClickListener = clickListener;
        }


    }


public void getHamperRequests(){


    FirebaseRecyclerOptions<HamperRequest> options =
            new FirebaseRecyclerOptions.Builder<HamperRequest>()
                    .setQuery(foodHamperRef,HamperRequest.class)
                    .build();

     hamperAdapter = new FirebaseRecyclerAdapter<HamperRequest,RequestsViewholder>(options) {

        @Override
        protected void onBindViewHolder(RequestsViewholder holder, int position,HamperRequest model) {

            holder.name.setText( String.valueOf(model.getName()));
            holder.location.setText( String.valueOf(model.getLocation()));
            holder.infants.setText( String.valueOf(model.getInfants()));
            holder.familyNumber.setText( String.valueOf(model.getPeople()));
            holder.lastMealDate.setText( String.valueOf(model.getLastMealDate()));
            holder.allergies.setText( String.valueOf(model.getAllergies()));


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                   String contentId= hamperAdapter.getRef(position).getKey();

                    return true;
                }
            });

        }
        @Override
        public RequestsViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.request_layout, parent, false);
            final RequestsViewholder vh = new RequestsViewholder(view);
            context = parent.getContext();

            vh.setOnClickListener(new RequestsViewholder.ClickListener() {
                @Override
                public void onItemClick(View view, int position) {



                }
                @Override
                public void onItemLongClick(View view, int position) {


                    //    Toast.makeText(getActivity(), "Item long clicked at " + position, Toast.LENGTH_SHORT).show();
                }



            });
            return vh;
        }





    };
/*
        mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = mAdapter.getItemCount();
                int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    layoutManager.scrollToPosition(positionStart);
                }
            }
        });
*/
    //  mAdapter.startListening();
    // mRecyclerView.setLayoutManager(layoutManager);
        requestsRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
}







    public static class RequestsViewholder extends RecyclerView.ViewHolder{



        public TextView name,id,location,infants,allergies,phoneNumber,lastMealDate,familyNumber;
        public  RequestsViewholder(View v) {
            super(v);

            name=(TextView) v.findViewById(R.id.beneficiary_name);
            id=(TextView) v.findViewById(R.id.title);
           location=(TextView) v.findViewById(R.id.location);
           // phoneNumber=(TextView) v.findViewById(R.id.title);
           infants=(TextView) v.findViewById(R.id.infants);
            lastMealDate=(TextView) v.findViewById(R.id.last_estimated_meal);
           familyNumber=(TextView) v.findViewById(R.id.beneficiaries_number);
            allergies=(TextView) v.findViewById(R.id.allergies);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickListener.onItemLongClick(v, getAdapterPosition());
                    return true;
                }
            });

        }


        private RequestsViewholder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener{
            public void onItemClick(View view, int position);
            public void onItemLongClick(View view, int position);
        }
        public void setOnClickListener(RequestsViewholder.ClickListener clickListener){
            mClickListener = clickListener;
        }




    }

    private ItemTouchHelper.Callback createHelperCallback() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            //not used, as the first parameter above is 0
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {

                swipedPosition = viewHolder.getAdapterPosition();
                //get object key


                return true;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {

                 //TODO  depending on what kind of volunteer is logged on  pick the right references

                String requestId = mAdapter.getRef(swipedPosition).getKey();

                HamperRequest hamperRequest = hamperAdapter.getItem(swipedPosition);
                DatabaseReference deleteEntryRef = foodHamperRef.child(requestId);


                //

                if (swipeDir == ItemTouchHelper.RIGHT) {
                    //whatever code you want the swipe to perform


                  // rejectHamperRequest();


                }
                if (swipeDir == ItemTouchHelper.LEFT) {
                    //whatever code you want the swipe to perform
                    //store object in attended node with the same key


                }}
            };
        return simpleItemTouchCallback;
        }

        private void acceptHamperRequest(DatabaseReference deleteEntryRef,String requestId,HamperRequest hamperRequest){

            //store object in reject node with the same key
            acceptedHamperRef.child(requestId).setValue(hamperRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //TODO toast to completion
                    hamperAdapter.getRef(swipedPosition).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            deleteEntryRef.removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    //TODO toast to completion
                                }
                            });

                        }
                    });
                }
            });
//
        }
        private void rejectHamperRequest(DatabaseReference deleteEntryRef,String requestId,HamperRequest hamperRequest){


            rejectedHamperRef.child(requestId).setValue(hamperRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    //TODO toast to completion
                    hamperAdapter.getRef(swipedPosition).removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            deleteEntryRef.removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                    //TODO toast to completion
                                }
                            });
                        }
                    });
                }
            });
}

        private void acceptHousingRequest(DatabaseReference deleteEntryRef,String requestId,HousingRequest housingRequest){}
    private void rejectHousingRequest(DatabaseReference deleteEntryRef,String requestId,HousingRequest housingRequest){}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
