package com.eidland.auxilium.voice.only.activity;

import static com.eidland.auxilium.voice.only.helper.Helper.getFormattedText;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.eidland.auxilium.voice.only.AGApplication;
import com.eidland.auxilium.voice.only.Interface.AGEventHandler;
import com.eidland.auxilium.voice.only.Interface.ItemClickListener1;
import com.eidland.auxilium.voice.only.R;
import com.eidland.auxilium.voice.only.adapter.AdapterComment;
import com.eidland.auxilium.voice.only.adapter.AdapterGame;
import com.eidland.auxilium.voice.only.adapter.AdapterGift;
import com.eidland.auxilium.voice.only.adapter.AdapterLeadUser;
import com.eidland.auxilium.voice.only.adapter.AdapterSeat;
import com.eidland.auxilium.voice.only.adapter.ViewerAdapter;
import com.eidland.auxilium.voice.only.helper.ConstantApp;
import com.eidland.auxilium.voice.only.helper.LeaderBoard;
import com.eidland.auxilium.voice.only.model.AnimationItem;
import com.eidland.auxilium.voice.only.model.Comment;
import com.eidland.auxilium.voice.only.model.Gift;
import com.eidland.auxilium.voice.only.model.GiftItem;
import com.eidland.auxilium.voice.only.model.Rooms;
import com.eidland.auxilium.voice.only.model.StaticConfig;
import com.eidland.auxilium.voice.only.model.User;
import com.eidland.auxilium.voice.only.model.Viewer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import pl.droidsonroids.gif.GifImageView;

public class LiveRoomActivity extends BaseActivity implements AGEventHandler, AdapterSeat.OnSeatClickListener, AdapterGift.OnGiftClickListener, AdapterGame.OnGameClickListener, ViewerAdapter.OnViewersClickListener {
    String type, SeatsName, AgainSeat, run;
    LinearLayout seatLayout;
    TextView onlineUserCount, broadName, sendGiftBtn, userAvailableCoin;
    ImageView sencmnt;
    LinearLayout commentBoxCircle;
    ProgressDialog progressDialog;
    String selectedGiftName = "flowers";
    int lastPos = 0;
    RelativeLayout lastselec;
    TextView ModUserRemove;
    Boolean muteClicked = false;
    ImageView micBtn, imgbroad;
    ImageView button1;
    ImageView leaveRoom;
    ViewerAdapter viewerAdapter;
    ArrayList<Viewer> onlineUserList = new ArrayList<>();
    ArrayList<Viewer> seatUsers = new ArrayList<>();
    LinearLayout online_layout;
    String hostuid, roomName;
    Spinner spinner;
    String selectuseruid;
    EditText commentBox;
    String Clickedseat = null;
    private final static Logger log = LoggerFactory.getLogger(LiveRoomActivity.class);
    CircleImageView popup_user;
    TextView popup_uname, eidlandpointcount;
    private volatile boolean mAudioMuted = false;
    ImageView userImage;
    private volatile int mAudioRouting = -1; // Default
    ChildEventListener eventListener;
    String imgUrl;
    RecyclerView viewers;
    ImageView bottom_action_end_call;
    ArrayList<Comment> comments;
    AdapterComment commentAdapter;
    ImageView roomGift, closeGiftBox, singleUserClose;
    LinearLayout crystal;
    TextView txtsinglename, txtsinglegiftsend, sendername, receivername, rewarded;
    RelativeLayout singlegift;
    LinearLayout mutelayout;
    LinearLayout micreqlayout;
    LinearLayout blocklayout;
    LinearLayout profilelayout;
    AdapterSeat adapterSeat;
    DatabaseReference userRef;
    FirebaseUser currentUser;
    Viewer selectedViewer = new Viewer();
    RelativeLayout singleUserBox;
    ImageView button;
    int height, width;
    boolean inactiveClick = false;
    String clickedOnlineUserUID;
    boolean isModerator = false;


    RelativeLayout animatedLayout;
    RelativeLayout backgroundGIFLayout;
    LottieAnimationView backgrundGIF;
    GifImageView simpleGift;
    boolean flag;
    ArrayList<Gift> giftList, leaderGiftList;

    ImageView gameButton;
    LinearLayout gamesLayout;
    RelativeLayout cardLoadingAnimationLayout;
    GifImageView cardLoadingAnimationGIF;
    ImageView closeGameDrawer;
    RelativeLayout displayCardLayout;
    GifImageView selectedCardGIF;
    ImageView displayCardImage;
    ImageView closeCard;
    ImageView minimizedCard;
    String cardImageURL, cardImageURL2;

    ImageView inviteButton;
    boolean isMod = false;
    String nameOfRoom;
    String inviteLink;
    String welcomeMsg;

    LinearLayout inputArea;

    ImageView lastImg;
    int selectedGiftAmount = 0;
    boolean isnotfirst = true;
    boolean hasEnteredRoom = true, newlyjoined = true;


    private void initKeyBoardListener() {
        final int MIN_KEYBOARD_HEIGHT_PX = 150;
        final View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private final Rect windowVisibleDisplayFrame = new Rect();
            private int lastVisibleDecorViewHeight;

            @Override
            public void onGlobalLayout() {
                decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
                final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();

                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight > visibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX) {
                        inputArea.setBackgroundColor(Color.rgb(238, 238, 228));
                        commentBox.setHintTextColor(Color.LTGRAY);
                        commentBox.setTextColor(Color.BLACK);
                        commentBoxCircle.setBackground(getDrawable(R.drawable.transparentwhitecircle));
                        sencmnt.setImageResource(R.drawable.ic_send_message_button);
                    } else if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                        inputArea.setBackgroundColor(Color.TRANSPARENT);
                        commentBox.setBackgroundColor(Color.TRANSPARENT);
                        commentBox.setHintTextColor(Color.WHITE);
                        commentBox.setTextColor(Color.WHITE);
                        commentBoxCircle.setBackground(getDrawable(R.drawable.transparentblackcircle));
                        sencmnt.setImageResource(R.drawable.ic_send_message_button_white);


                    }
                }
                lastVisibleDecorViewHeight = visibleDecorViewHeight;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_room);

        initKeyBoardListener();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setMessage("Your Room is being ready..");
        progressDialog.setCancelable(false);
        seatLayout = findViewById(R.id.seat_layout);

        inputArea = findViewById(R.id.input_box_area);

        leaveRoom = findViewById(R.id._leave);
        //   userImage = findViewById(R.id._userchatroom);
        micBtn = (ImageView) findViewById(R.id.mute_local_speaker_id);
        button1 = (ImageView) findViewById(R.id.switch_broadcasting_id);
        ModUserRemove = findViewById(R.id.block_text);
        bottom_action_end_call = (ImageView) findViewById(R.id.bottom_action_end_call);
        onlineUserCount = findViewById(R.id.online_user_count);
        online_layout = findViewById(R.id.online_layout);
        txtsinglegiftsend = findViewById(R.id.singlegiftsend);
        singlegift = findViewById(R.id.singlesendgift);
        mutelayout = findViewById(R.id.mutelayout);
        micreqlayout = findViewById(R.id.micreqlayout);
        blocklayout = findViewById(R.id.blocklayout);
        profilelayout = findViewById(R.id.profilelayout);
        txtsinglename = findViewById(R.id.txtnamepopup);
        eidlandpointcount = findViewById(R.id.eidland_point_count);
        singleUserClose = findViewById(R.id.close);
        simpleGift = findViewById(R.id.imggif);
        sendername = findViewById(R.id.sendername);
        rewarded = findViewById(R.id.sendername2);

        receivername = findViewById(R.id.receivername);
        popup_uname = findViewById(R.id.txtnamepopup);
        popup_user = findViewById(R.id.userimgpopup);
        animatedLayout = findViewById(R.id.animatedlayout);
        backgrundGIF = findViewById(R.id.backgrundGIF);
        backgroundGIFLayout = findViewById(R.id.backgroundGIFLayout);
        giftList = new ArrayList<>();
        leaderGiftList = new ArrayList<>();
        singleUserBox = findViewById(R.id.single_user_box);
        viewers = findViewById(R.id.viewersrecyler);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        sendGiftBtn = findViewById(R.id.send_gift);
        userAvailableCoin = findViewById(R.id.user_available_coin);
        spinner = findViewById(R.id.spinner);
        crystal = findViewById(R.id.giftslayout);
        closeGiftBox = findViewById(R.id.closegift);
        roomGift = findViewById(R.id.room_gift);
        commentBox = findViewById(R.id.comment_box);
        sencmnt = findViewById(R.id.sndcmnt);
        commentBoxCircle = findViewById(R.id.comment_box_circle);
        imgbroad = findViewById(R.id.hostimg);
        broadName = findViewById(R.id.room_name);

        gameButton = findViewById(R.id.game_button_icon);
        gamesLayout = findViewById(R.id.gameslayout);
        cardLoadingAnimationLayout = findViewById(R.id.card_loading_animation_layout);
        cardLoadingAnimationGIF = findViewById(R.id.card_loading_animation_GIF);
        closeGameDrawer = findViewById(R.id.closegamedrawer);
        displayCardLayout = findViewById(R.id.display_card_layout);
        selectedCardGIF = findViewById(R.id.selected_card_GIF);
        displayCardImage = findViewById(R.id.display_card_image);
        closeCard = findViewById(R.id.closecard);
        minimizedCard = findViewById(R.id.card_minimized);

        inviteButton = findViewById(R.id.invite_icon);


        try {
            onlineUserCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewDialogUser viewDialoguser = new ViewDialogUser(LiveRoomActivity.this, width, height);
                    //  viewDialoguser.showDialog(onlineUserList, (ViewerListAdapter.OnViewerClickListener) LiveRoomActivity.this);
                    //  ViewDialogUser viewDialoguser = new ViewDialogUser(LiveRoomActivity.this, width, height);
                    viewDialoguser.showDialog(onlineUserList);
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
        }

        closeGiftBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crystal.setVisibility(View.GONE);
                gameButton.setVisibility(View.VISIBLE);
                commentBox.setVisibility(View.VISIBLE);
            }
        });

        roomGift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedViewer.id = "cJupIaBOKXN8QqWzAQMQYFwHzVC3";
                selectedViewer.name = nameOfRoom;
                selectedViewer.photo = "https://auxiliumlivestreaming.000webhostapp.com/images/Eidlandhall.png";
                selectuseruid = "cJupIaBOKXN8QqWzAQMQYFwHzVC3";
                txtsinglename.setText(nameOfRoom);
                crystal.setVisibility(View.VISIBLE);
                gameButton.setVisibility(View.GONE);
                commentBox.setVisibility(View.GONE);
            }
        });
        singleUserClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleUserBox.setVisibility(View.GONE);
            }
        });

        userAvailableCoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LiveRoomActivity.this, WalletActivity.class));
            }
        });

        leaveRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dialog dialog = new Dialog(LiveRoomActivity.this);
                dialog.setContentView(R.layout.layout_custom_dialog);
                LinearLayout linearLayout = dialog.findViewById(R.id.alert_root);
                linearLayout.setMinimumWidth((int) (width * 0.8));
                dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_corner);
                dialog.setCancelable(false);

                ImageView imageView = dialog.findViewById(R.id.dialog_icon);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_exit);

                TextView msg = dialog.findViewById(R.id.msg);
                msg.setVisibility(View.VISIBLE);
                msg.setText("Are you sure you want to leave the room?");
                RelativeLayout areatop = dialog.findViewById(R.id.topdialogbutton);
                TextView positive = dialog.findViewById(R.id.positive_btn);
                positive.setVisibility(View.VISIBLE);
                positive.setText("Leave");
                areatop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        run = "1";
                        progressDialog.show();
                        EndMeeting();
                    }
                });
                RelativeLayout areabottom = dialog.findViewById(R.id.bottomdialogbutton);
                TextView negative = dialog.findViewById(R.id.negative_btn);
                negative.setVisibility(View.VISIBLE);
                negative.setText("Cancel");
                areabottom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });

        gameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gamesLayout.setVisibility(View.VISIBLE);
                minimizedCard.setVisibility(View.INVISIBLE);
            }
        });

        closeGameDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gamesLayout.setVisibility(View.GONE);
                if (cardImageURL != null) {
                    minimizedCard.setVisibility(View.VISIBLE);
                }
            }
        });

        closeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayCardLayout.setVisibility(View.GONE);
                minimizedCard.setVisibility(View.VISIBLE);
                seatLayout.setVisibility(View.VISIBLE);
                commentBox.setVisibility(View.VISIBLE);
                roomGift.setVisibility(View.VISIBLE);
                gameButton.setVisibility(View.VISIBLE);

            }
        });

        minimizedCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minimizedCard.setVisibility(View.INVISIBLE);
                displayCardLayout.setVisibility(View.VISIBLE);
                commentBox.setVisibility(View.GONE);
                roomGift.setVisibility(View.GONE);
                gameButton.setVisibility(View.GONE);
                seatLayout.setVisibility(View.GONE);
            }
        });

        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    FirebaseDatabase.getInstance().getReference().child("AllRooms").child(roomName).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Rooms room = snapshot.getValue(Rooms.class);
//                            Toast.makeText(getApplicationContext(), String.valueOf(snapshot.getValue()), Toast.LENGTH_LONG).show();
                            // not entering the if condition
                            assert room != null;
                            if (room.getInviteLink() == null) {
                                String link = "https://eidland.page.link/invite/?roomname=" + room.getRoomname();
                                FirebaseDynamicLinks.getInstance().createDynamicLink()
                                        .setLink(Uri.parse(link))
                                        .setDomainUriPrefix("https://eidland.page.link")
                                        .setAndroidParameters(
                                                new DynamicLink.AndroidParameters.Builder("com.example.android")
                                                        .setMinimumVersion(125)
                                                        .build())
                                        .buildShortDynamicLink()
                                        .addOnSuccessListener(new OnSuccessListener<ShortDynamicLink>() {
                                            @Override
                                            public void onSuccess(ShortDynamicLink shortDynamicLink) {
                                                try {
                                                    Uri mInvitationUrl = shortDynamicLink.getShortLink();
                                                    Intent intent = new Intent(String.valueOf(mInvitationUrl));
                                                    intent.setAction(Intent.ACTION_VIEW);
                                                    assert mInvitationUrl != null;
                                                    room.setInviteLink(mInvitationUrl.toString());
                                                    FirebaseDatabase.getInstance().getReference().child("AllRooms").child(room.getRoomname()).child("inviteLink").setValue(room.getInviteLink());
                                                    FirebaseDatabase.getInstance().getReference().child("RoomInvites").child(room.getInviteLink()).setValue(room.getRoomname());
                                                } catch (Exception e) {
                                                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }

//                            room.setInviteLink(snapshot.getValue().toString());
//                            Toast.makeText(getApplicationContext(), String.valueOf(snapshot.getValue()), Toast.LENGTH_LONG).show();

                            Intent sendIntent = new Intent("com.eidland.auxilium.voice.only.activity.LiveRoomActivity");
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra("UserName", room.name);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hey, we're having a pretty interesting discussion on EidLand! Use this link to join:\n" + room.getInviteLink());
                            sendIntent.setType("text/plain");
                            Intent shareIntent = Intent.createChooser(sendIntent, null);
                            startActivity(shareIntent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        comments = new ArrayList<>();
        RecyclerView commentRecyclerView = findViewById(R.id.live_comment_recyler);
        commentAdapter = new AdapterComment(this, comments, new ItemClickListener1() {
            @Override
            public void onPositionClicked(View view, final int position) {
            }
        });
        commentRecyclerView.hasFixedSize();
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commentRecyclerView.setAdapter(commentAdapter);
        welcomeMsg = getIntent().getStringExtra("welcomemsg");
        nameOfRoom = getIntent().getStringExtra("UserName");
        final Comment comment = new Comment();
        comment.setComment(welcomeMsg);
        comment.setName("Eidland Staff \uD83E\uDD73");
        imgUrl = getIntent().getStringExtra("profile");
        comment.setUserphoto(imgUrl);
        comments.add(comment);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        type = getIntent().getStringExtra("User");

        imgUrl = getIntent().getStringExtra("profile");

        Glide.with(this).load(imgUrl).error(R.drawable.userprofile).placeholder(R.drawable.userprofile).into(imgbroad);
        broadName.setText(nameOfRoom + " \uD83E\uDD4A\uD83C\uDFC6\uD83C\uDFC5");

        if (type.equals("Host")) {
            roomName = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ROOM_NAME);
            hostuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Viewer viewer = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), imgUrl, StaticConfig.user.getEmail(), "host", StaticConfig.user.getReceivedCoins(), config().mUid);
            FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(viewer);
            AgainSeat = "seat0";
          /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!LiveRoomActivity.this.isDestroyed())
                    Glide.with(LiveRoomActivity.this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(userImage);
            } else {
                Glide.with(LiveRoomActivity.this).load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).into(userImage);
            }*/
            onlineUserList.clear();
            setOnlineMembers();
            getToken(true);
        } else {
            roomName = getIntent().getStringExtra(ConstantApp.ACTION_KEY_ROOM_NAME);
            hostuid = getIntent().getStringExtra("userid");
            Viewer comment1 = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), StaticConfig.user.getImageurl(), StaticConfig.user.getEmail(), StaticConfig.user.getName(), StaticConfig.user.getReceivedCoins(), config().mUid);
            FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(comment1);
           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!LiveRoomActivity.this.isDestroyed())
                    Glide.with(LiveRoomActivity.this).load(StaticConfig.user.getImageurl()).into(userImage);
            } else {
                Glide.with(LiveRoomActivity.this).load(StaticConfig.user.getImageurl()).into(userImage);
            }*/

            getToken(false);

        }
        CheckModeratorGame(currentUser.getUid());


        sencmnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(commentBox.getText().toString())) {
                    Comment comment1 = new Comment(StaticConfig.user.getName(), commentBox.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getUid(), false, ".", ".", StaticConfig.user.getImageurl());
                    FirebaseDatabase.getInstance().getReference().child("livecomments").child(roomName).push().setValue(comment1);
                    commentBox.setText("");
                }
            }
        });
        selectuseruid = hostuid;

        FirebaseDatabase.getInstance().getReference("Mods").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(currentUser.getUid())) {
                    isModerator = true;
                }else{
                    isModerator = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        singlegift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singleUserBox.setVisibility(View.GONE);

                crystal.setVisibility(View.VISIBLE);
                gamesLayout.setVisibility(View.GONE);
                commentBox.setVisibility(View.GONE);
            }
        });

        mutelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Not Applicable with your current user privilege!", Toast.LENGTH_SHORT).show();
            }
        });

        micreqlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isModerator) {
                    if (clickedOnlineUserUID == null) {
                        Toast.makeText(getApplicationContext(), "Select user from online user list!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (seatUsers.size() >= 10) {
                        Toast.makeText(getApplicationContext(), "No available seat!", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        for (Viewer viewer : seatUsers) {
                            if (viewer.id.equals(clickedOnlineUserUID)) {
                                Toast.makeText(getApplicationContext(), "User Already on seat!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        FirebaseDatabase.getInstance().getReference().child("MicRequests").child(roomName).child(clickedOnlineUserUID).onDisconnect().removeValue();
                        String modName = "Moderator";
                        try{
                            modName = StaticConfig.user.getName();
                        }catch (Exception e){}
                        FirebaseDatabase.getInstance().getReference().child("MicRequests").child(roomName).child(clickedOnlineUserUID).setValue(modName + " invited you to take the seat!");
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Not Applicable with your current user privilege!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        requestListener();

        blocklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Not Applicable with your current user privilege!", Toast.LENGTH_SHORT).show();
            }
        });

        profilelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LiveRoomActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).onDisconnect().removeValue();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userRef.child(user.getUid()).

                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        try {
                            User user = snapshot.getValue(User.class);
                            StaticConfig.user = user;
                            userAvailableCoin.setText(getFormattedText(StaticConfig.user.getCoins()));
                        } catch (Exception e) {

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        System.out.println(error);
                    }
                });

        setOnlineMembers();

        setNameAllSeats();

        giftsListener();

        gameListener();

        FirebaseDatabase.getInstance().getReference().child("livecomments").child(roomName).orderByKey().limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childd : dataSnapshot.getChildren()) {
                    comments.add(childd.getValue(Comment.class));
                }
                commentAdapter.notifyDataSetChanged();
                commentRecyclerView.smoothScrollToPosition(comments.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        RecyclerView viewers = findViewById(R.id.viewersrecyler);
        viewers.hasFixedSize();
        viewers.setLayoutManager(new LinearLayoutManager(LiveRoomActivity.this, LinearLayoutManager.HORIZONTAL, true));
        viewerAdapter = new ViewerAdapter(LiveRoomActivity.this, this, onlineUserList);
        viewers.setAdapter(viewerAdapter);
        viewerAdapter.notifyDataSetChanged();

        RecyclerView seatRecycler = findViewById(R.id.seat_recycler);
        seatRecycler.setHasFixedSize(true);
        GridLayoutManager seatLayoutManager = new GridLayoutManager(LiveRoomActivity.this, 5, GridLayoutManager.VERTICAL, false);
        adapterSeat = new AdapterSeat(LiveRoomActivity.this, this, roomName);
        seatRecycler.setLayoutManager(seatLayoutManager);
        adapterSeat.notifyDataSetChanged();
        seatRecycler.setAdapter(adapterSeat);


        RecyclerView giftRecycler = findViewById(R.id.gift_recycler);
        giftRecycler.setHasFixedSize(true);
        GridLayoutManager giftLayoutManager = new GridLayoutManager(LiveRoomActivity.this, 2, GridLayoutManager.HORIZONTAL, false);
        AdapterGift adapterGift = new AdapterGift(LiveRoomActivity.this, this, width / 4);
        giftRecycler.setLayoutManager(giftLayoutManager);
        adapterGift.notifyDataSetChanged();
        giftRecycler.setAdapter(adapterGift);

        RecyclerView gameRecycler = findViewById(R.id.game_recycler);
        giftRecycler.setHasFixedSize(true);
        GridLayoutManager gameLayoutManager = new GridLayoutManager(LiveRoomActivity.this, 1, GridLayoutManager.HORIZONTAL, false);
        AdapterGame adapterGame = new AdapterGame(LiveRoomActivity.this, this, width);
        gameRecycler.setLayoutManager(gameLayoutManager);
        adapterGame.notifyDataSetChanged();
        gameRecycler.setAdapter(adapterGame);


        sendGiftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectuseruid == null)
                    Toast.makeText(getApplicationContext(), "Please select a speaker first", Toast.LENGTH_SHORT).show();

                if (!selectuseruid.equals(currentUser.getUid())) {
                    if (selectuseruid.equals("cJupIaBOKXN8QqWzAQMQYFwHzVC3"))
                        Toast.makeText(getApplicationContext(), "Please select a speaker first", Toast.LENGTH_SHORT).show();
                    else {
                        if (selectedGiftAmount > 0) {
                            Long curnt = Long.parseLong(StaticConfig.user.getCoins());
                            if (curnt > selectedGiftAmount) {
                                crystal.setVisibility(View.GONE);
                                gameButton.setVisibility(View.VISIBLE);
                                commentBox.setVisibility(View.VISIBLE);
                                FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser.getUid()).runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                                        try {
                                            User user = currentData.getValue(User.class);
                                            user.coins = String.valueOf(Long.parseLong(user.coins) - selectedGiftAmount);
                                            user.receivedCoins = String.valueOf(Long.parseLong(user.receivedCoins) + selectedGiftAmount);
                                            currentData.setValue(user);
                                        } catch (Exception e) {
                                            System.out.println(e);
                                        }
                                        return Transaction.success(currentData);
                                    }

                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

                                        try {
                                            User user = currentData.getValue(User.class);
                                            StaticConfig.user = user;
                                            userAvailableCoin.setText(getFormattedText(user.coins));

                                        } catch (Exception e) {
                                            System.out.println(e);
                                        }
                                    }
                                });
                                FirebaseDatabase.getInstance().getReference().child("Users").child(selectuseruid).runTransaction(new Transaction.Handler() {
                                    @NonNull
                                    @Override
                                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                        try {
                                            User user = currentData.getValue(User.class);
                                            assert user != null;
                                            user.receivedCoins = String.valueOf(Long.parseLong(user.receivedCoins) + selectedGiftAmount);
                                            currentData.setValue(user);
                                        } catch (Exception e) {
                                            System.out.println(e);
                                        }
                                        return Transaction.success(currentData);
                                    }

                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                                        System.out.println(error);
                                    }
                                });
                                sendGift(new Gift(selectedGiftName, selectedGiftAmount, currentUser.getUid(), StaticConfig.user.name, StaticConfig.user.imageurl, selectuseruid, selectedViewer.getName(), selectedViewer.photo, System.currentTimeMillis()));
                            } else {
                                if (selectedGiftAmount == 0) {
                                    Toast.makeText(getApplicationContext(), "No Gift is selected", Toast.LENGTH_SHORT).show();
                                } else
                                    Toast.makeText(getApplicationContext(), "Low Balance Please Purchase Coins", Toast.LENGTH_SHORT).show();
                            }
                        } else
                            Toast.makeText(getApplicationContext(), "No Gift is selected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "You can not send gift to yourself", Toast.LENGTH_SHORT).show();
                }
            }
        });


        FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                seatUsers.clear();
                if (snapshot.exists()) {
                    try {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            Viewer viewer = snap.getValue(Viewer.class);
                            seatUsers.add(viewer);
                        }

                    } catch (Exception e) {

                    }
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onSeatClick(int position) {
        clickedOnlineUserUID = null;
        if (!inactiveClick) {
            CheckSeats("seat" + position);
            Clickedseat = "seat" + position;
        }
    }

    @Override
    public void onGiftClick(int position, ImageView icon, RelativeLayout selec) {
        if (position != lastPos) {
            if (lastselec != null) {
                lastselec.setVisibility(View.GONE);
            }
        }

        GiftItem giftItem = ConstantApp.giftList().get(position);
        try {
            if (lastImg != null) lastImg.setImageResource(0);
            lastImg = icon;
            selec.setVisibility(View.VISIBLE);

            lastPos = position;
            lastselec = selec;
        } catch (Exception e) {
            System.out.println(e);
        }

        //  icon.setImageResource(R.drawable.ic_check_1_gift_select);
        selectedGiftName = giftItem.name;
        selectedGiftAmount = giftItem.amount;
    }

    public void CheckModerator(final String st, String clickeduser, final String seat) {
        FirebaseDatabase.getInstance().getReference("Mods").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(st)) {
                    ModUserRemove.setText("Remove\nParticipant");
                    blocklayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(seat).removeValue();
                            Toast.makeText(LiveRoomActivity.this, "user removed", Toast.LENGTH_LONG).show();
                            singleUserBox.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void inist(String token) {
        event().addEventHandler(this);
        Intent i = getIntent();
        int cRole = i.getIntExtra(ConstantApp.ACTION_KEY_CROLE, 0);
        if (cRole == 0) {
            throw new RuntimeException("Should not reach here");
        }

        String roomName = i.getStringExtra(ConstantApp.ACTION_KEY_ROOM_NAME);
        doConfigEngine(cRole);
        ImageView button1 = findViewById(R.id.switch_broadcasting_id);

        if (isBroadcaster(cRole)) {
            broadcasterUI(button1, micBtn);
        } else {
            audienceUI(button1, micBtn);
        }
        worker().joinChannel(token, roomName, config().mUid);
        optional();
    }

    @Override
    protected void initUIandEvent() {
        optional();
    }

    public void getToken(final boolean isHost) {

        RequestQueue MyRequestQueue = Volley.newRequestQueue(LiveRoomActivity.this);

        StringRequest MyStringRequest = new StringRequest(Request.Method.GET, "https://auxilium2.herokuapp.com/access_token?channel=" + roomName, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String token = jsonObject.getString("token");
                    if (isHost) {
                        Rooms room = new Rooms(nameOfRoom, imgUrl, hostuid, token, "0", roomName, "0", "0", "init", inviteLink, "Welcome! tap a seat to start speaking", "general");
                        FirebaseDatabase.getInstance().getReference().child("AllRooms").child(roomName).setValue(room);
                        SeatsName = "seat1";
                        Viewer viewer = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), imgUrl, FirebaseAuth.getInstance().getCurrentUser().getEmail(), nameOfRoom, StaticConfig.user.getReceivedCoins(), config().mUid);
                        FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(SeatsName).setValue(viewer);
                        AgainSeat = SeatsName;
                        inist(token);
                    } else {
                        inist(token);
                    }

                } catch (JSONException e) {
                    //   e.printStackTrace();
                    Log.e("roor", e.getLocalizedMessage() + "d");
                    Toast.makeText(LiveRoomActivity.this, "Json", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                return MyData;
            }
        };


        MyRequestQueue.add(MyStringRequest);
    }

    private void setNameAllSeats() {
        FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    Viewer seat00 = snapshot.getValue(Viewer.class);
                    String UserID = seat00.getId();
                    if (UserID.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        doSwitchToBroadcaster(false);
                        System.out.println("doSwitchToBroadcaster");
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void requestListener() {
        FirebaseDatabase.getInstance().getReference().child("MicRequests").child(roomName).child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String msge = snapshot.getValue(String.class);
                    Dialog dialog = new Dialog(LiveRoomActivity.this);

                    dialog.setContentView(R.layout.layout_custom_dialog);
                    LinearLayout linearLayout = dialog.findViewById(R.id.alert_root);
                    linearLayout.setMinimumWidth((int) (width * 0.8));
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_corner);
                    dialog.setCancelable(false);

                    ImageView imageView = dialog.findViewById(R.id.dialog_icon);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageResource(R.drawable.microphone);

                    TextView msg = dialog.findViewById(R.id.msg);
                    msg.setVisibility(View.VISIBLE);
                    msg.setText(msge);

                    TextView positive = dialog.findViewById(R.id.positive_btn);
                    positive.setVisibility(View.VISIBLE);
                    positive.setText("Accept");

                    Dialog finalDialog = dialog;
                    positive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finalDialog.dismiss();
                            FirebaseDatabase.getInstance().getReference().child("MicRequests").child(roomName).child(currentUser.getUid()).removeValue();

                        }
                    });

                    TextView negative = dialog.findViewById(R.id.negative_btn);
                    negative.setVisibility(View.VISIBLE);
                    negative.setText("Reject");
                    Dialog finalDialog1 = dialog;
                    negative.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finalDialog1.dismiss();
                            FirebaseDatabase.getInstance().getReference().child("MicRequests").child(roomName).child(currentUser.getUid()).removeValue();

                        }
                    });

                    if(!isFinishing()) {
                        dialog.show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void CheckSeats(final String seats) {
        if (AgainSeat == null) {
            Query query = FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(seats);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        try {
                            Viewer viewer = snapshot.getValue(Viewer.class);
                            selectuseruid = viewer.getId();
                            selectedViewer = viewer;
                            CheckModerator(currentUser.getUid(), selectuseruid, seats);
                            Glide.with(getApplicationContext()).load(viewer.getPhotoUrl()).into(popup_user);
                            txtsinglename.setText(viewer.getName());
                            eidlandpointcount.setText(viewer.getRecievedCoins());
                            singleUserBox.setVisibility(View.VISIBLE);
                            eidlandpointcount.setText(viewer.getRecievedCoins());
                        } catch (Exception e) {
                            System.out.println(e);
                        }

                    } else {

                        boolean checkPermissionResult = checkSelfPermissions();
                        if (checkPermissionResult) {


                            inactiveClick = true;
                            FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(seats).runTransaction(new Transaction.Handler() {
                                @NonNull
                                @Override
                                public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                                    Viewer viewer = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), StaticConfig.user.getImageurl(), StaticConfig.user.getEmail(), StaticConfig.user.getName(), StaticConfig.user.getReceivedCoins(), config().mUid);

                                    try {
                                        Viewer user = currentData.getValue(Viewer.class);
                                        if (user == null) {
                                            currentData.setValue(viewer);
                                        }
                                    } catch (Exception e) {
                                        System.out.println(e);
                                    }
                                    return Transaction.success(currentData);
                                }

                                @Override
                                public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

                                    try {
                                        Viewer user = currentData.getValue(Viewer.class);
                                        FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(seats).onDisconnect().removeValue();
                                        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(user.id)) {
                                            doSwitchToBroadcaster(true);
                                            AgainSeat = seats;
                                        } else {
                                            doSwitchToBroadcaster(false);
                                            AgainSeat = null;
                                        }
                                        inactiveClick = false;
                                    } catch (Exception e) {
                                        System.out.println(e);
                                    }
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            Query query = FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(seats);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        Viewer viewer = snapshot.getValue(Viewer.class);
                        selectuseruid = viewer.getId();

                        selectedViewer = viewer;
                        CheckModerator(currentUser.getUid(), selectuseruid, seats);
                        Glide.with(getApplicationContext()).load(viewer.getPhotoUrl()).placeholder(R.drawable.appicon).error(R.drawable.appicon).into(popup_user);
                        txtsinglename.setText(viewer.getName());
                        singleUserBox.setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void giftsListener() {
        FirebaseDatabase.getInstance().getReference().child("gifts").child(roomName).orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (isnotfirst) {
                    giftList.clear();
                    leaderGiftList.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Gift gift = dataSnapshot1.getValue(Gift.class);

                            assert gift != null;
                            if (gift.getGift() != null && gift.getSenderName() != null) {
                                giftList.add(gift);
                            }
                            if (gift.getGift() != null && gift.getSenderName() != null) {
                                leaderGiftList.add(gift);
                            }
                        }

                        int index = giftList.size() - 1;
                        giftAnimation(giftList.get(index).getGift(), giftList.get(index), giftList.get(index).getReceiverName());

                        LeaderBoard leaderBoard = new LeaderBoard(leaderGiftList, hostuid);

                        RecyclerView topSpeakerRecycler = findViewById(R.id.top_speaker);
                        topSpeakerRecycler.setHasFixedSize(true);
                        LinearLayoutManager giftLayoutManager = new LinearLayoutManager(LiveRoomActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        AdapterLeadUser adapterLeader = new AdapterLeadUser(LiveRoomActivity.this, leaderBoard.getTopSpeaker());
                        topSpeakerRecycler.setLayoutManager(giftLayoutManager);
                        adapterLeader.notifyDataSetChanged();
                        topSpeakerRecycler.setAdapter(adapterLeader);

                        RecyclerView topContributorRecycler = findViewById(R.id.top_contributor);
                        topContributorRecycler.setHasFixedSize(true);
                        LinearLayoutManager contributorLayoutManager = new LinearLayoutManager(LiveRoomActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        AdapterLeadUser adapterLeaderContributor = new AdapterLeadUser(LiveRoomActivity.this, leaderBoard.getTopContributor());
                        topContributorRecycler.setLayoutManager(contributorLayoutManager);
                        adapterLeader.notifyDataSetChanged();
                        topContributorRecycler.setAdapter(adapterLeaderContributor);

                        if (hasEnteredRoom) {
                            rewarded.setVisibility(View.GONE);
                            simpleGift.setImageResource(R.drawable.hello_pana);
                            backgrundGIF.setImageResource(R.drawable.fireworks_gif);
                            backgroundGIFLayout.setVisibility(View.VISIBLE);
                            sendername.setText("Hey " + StaticConfig.user.getName() + "!");
                            receivername.setText("Welcome to " + nameOfRoom);
                            hasEnteredRoom = false;
                        }
                    }

                } else
                    isnotfirst = true;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void giftAnimation(String id, Gift gift, String receiver) {

        for (AnimationItem animationItem :
                ConstantApp.animationItems()) {
            if (animationItem.name.equals(id)) {
                simpleGift.setImageResource(animationItem.giftIconId);
                backgrundGIF.setAnimation(animationItem.gifIconId);
                backgrundGIF.playAnimation();
                rewarded.setVisibility(View.VISIBLE);
                backgroundGIFLayout.setVisibility(View.VISIBLE);
            }
        }
        sendername.setText(gift.getSenderName());
        receivername.setText(receiver);

        Handler enterScreen = new Handler();
        enterScreen.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    animatedLayout.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.enter);
                    animatedLayout.setAnimation(animation);
                }
            }
        }, 1500);

        Handler exitScreen = new Handler();
        exitScreen.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.exit);
                animatedLayout.setAnimation(animation2);
                animatedLayout.setVisibility(View.INVISIBLE);
                backgroundGIFLayout.setVisibility(View.GONE);
                try {
                    giftList.remove(0);
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }, 3000);
    }

    private void sendGift(Gift gift) {
        FirebaseDatabase.getInstance().getReference().child("gifts").child(roomName).push().setValue(gift.toMap());
        Comment comment = new Comment(gift.getSenderName(), "Rewarded to " + txtsinglename.getText().toString() + "\n", FirebaseAuth.getInstance().getCurrentUser().getUid(), true, selectedGiftName, "1", StaticConfig.user.getImageurl());
        FirebaseDatabase.getInstance().getReference().child("livecomments").child(roomName).push().setValue(comment);
    }

    public void setOnlineMembers() {
        FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                onlineUserList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        try {
                            Viewer viewer = data.getValue(Viewer.class);
                            onlineUserList.add(viewer);
                        } catch (Exception e) {
                            System.out.println(e);
                        }
                    }
                }
                viewerAdapter.notifyDataSetChanged();
                onlineUserCount.setText(onlineUserList.size() + " Online");
                RecyclerView viewers = findViewById(R.id.viewersrecyler);
                viewers.hasFixedSize();
                viewers.setLayoutManager(new LinearLayoutManager(LiveRoomActivity.this, LinearLayoutManager.HORIZONTAL, true));
                ViewerAdapter viewerAdapter = new ViewerAdapter(LiveRoomActivity.this, LiveRoomActivity.this,onlineUserList);
                viewers.setAdapter(viewerAdapter);
                viewerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        eventListener = new ChildEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).addChildEventListener(eventListener);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    private Handler mMainHandler;

    private static final int UPDATE_UI_MESSAGE = 0x1024;

    StringBuffer mMessageCache = new StringBuffer();

    private void notifyMessageChanged(String msg) {
        if (mMessageCache.length() > 10000) { // drop messages
            mMessageCache = new StringBuffer(mMessageCache.substring(10000 - 40));
        }

        mMessageCache.append(System.currentTimeMillis()).append(": ").append(msg).append("\n"); // append timestamp for messages

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                if (mMainHandler == null) {
                    mMainHandler = new Handler(getMainLooper()) {
                        @Override
                        public void handleMessage(Message msg) {
                            super.handleMessage(msg);

                            if (isFinishing()) {
                                return;
                            }

                            switch (msg.what) {
                                case UPDATE_UI_MESSAGE:
                                    String content = (String) (msg.obj);
                                    break;
                                default:
                                    break;
                            }
                        }
                    };
                }

                mMainHandler.removeMessages(UPDATE_UI_MESSAGE);
                Message envelop = new Message();
                envelop.what = UPDATE_UI_MESSAGE;
                envelop.obj = mMessageCache.toString();
                mMainHandler.sendMessageDelayed(envelop, 1000l);
            }
        });
    }

    private void optional() {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }


    public void onSwitchSpeakerClicked(View view) {
        RtcEngine rtcEngine = rtcEngine();
        rtcEngine.setEnableSpeakerphone(mAudioRouting != 3);
    }

    private void doConfigEngine(int cRole) {
        worker().configEngine(cRole);
    }

    private boolean isBroadcaster(int cRole) {
        return cRole == Constants.CLIENT_ROLE_BROADCASTER;
    }

    @Override
    protected void deInitUIandEvent() {
        worker().leaveChannel(config().mChannel);
        event().removeEventHandler(this);
    }

    public void onEndCallClicked(View view) {

        if (AgainSeat != null) {
            doSwitchToBroadcaster(false);
            if (flag) {
                Glide.with(LiveRoomActivity.this).load(R.drawable.ic_mic_on).into(button);
            }
            FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(AgainSeat).removeValue();
            AgainSeat = null;
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goProfile() {


        if (AgainSeat != null) {

            FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(AgainSeat).removeValue();

            AgainSeat = null;
            Intent intent = new Intent(LiveRoomActivity.this, ProfileActivity.class);
            startActivity(intent);


        } else {

            Intent intent = new Intent(LiveRoomActivity.this, ProfileActivity.class);
            startActivity(intent);


        }

        try {
            FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    private void goBack() {

        Dialog dialog = new Dialog(LiveRoomActivity.this);
        dialog.setContentView(R.layout.layout_custom_dialog);
        LinearLayout linearLayout = dialog.findViewById(R.id.alert_root);
        linearLayout.setMinimumWidth((int) (width * 0.8));
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_corner);
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.dialog_icon);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.drawable.ic_exit_1popup_exit);

        TextView msg = dialog.findViewById(R.id.msg);
        msg.setVisibility(View.VISIBLE);
        msg.setText("Are you sure you want to leave current session?");

        TextView positive = dialog.findViewById(R.id.positive_btn);
        positive.setVisibility(View.VISIBLE);
        positive.setText("Yes");
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (AgainSeat != null) {
                    doSwitchToBroadcaster(false);
                    FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(AgainSeat).removeValue();
                    AgainSeat = null;
                    Intent intent = new Intent(LiveRoomActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(LiveRoomActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                try {
                    FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        });

        TextView negative = dialog.findViewById(R.id.negative_btn);
        negative.setVisibility(View.VISIBLE);
        negative.setText("No");
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void EndMeeting() {
        FirebaseDatabase.getInstance().getReference().child("Viewers").child(roomName).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
        if (AgainSeat != null) {
            FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(AgainSeat).removeValue();
            log.info("onBackPressed");
            progressDialog.cancel();
            finish();
            LiveRoomActivity.super.onBackPressed();
            run = "1";

        } else {
            progressDialog.cancel();
            finish();
            LiveRoomActivity.super.onBackPressed();
            run = "1";
        }


    }


    private void doSwitchToBroadcaster(boolean broadcaster) {
        final int uid = config().mUid;
        log.debug("doSwitchToBroadcaster " + (uid & 0XFFFFFFFFL) + " " + broadcaster);

        if (broadcaster) {
            doConfigEngine(Constants.CLIENT_ROLE_BROADCASTER);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    ImageView button1 = findViewById(R.id.switch_broadcasting_id);
                    ImageView button2 = findViewById(R.id.mute_local_speaker_id);
                    broadcasterUI(button1, button2);
                }
            }, 1000); // wait for reconfig engine
        } else {
            stopInteraction(uid);
        }
    }

    private void stopInteraction(final int uid) {
        doConfigEngine(Constants.CLIENT_ROLE_AUDIENCE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ImageView button1 = findViewById(R.id.switch_broadcasting_id);
                ImageView button2 = findViewById(R.id.mute_local_speaker_id);
                audienceUI(button1, button2);
            }
        }, 1000); // wait for reconfig engine
    }

    private void audienceUI(ImageView button1, ImageView button2) {
        button1.setTag(null);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                doSwitchToBroadcaster(tag == null || !((boolean) tag));
            }
        });
        button1.clearColorFilter();
        button2.setTag(null);
        button2.setVisibility(View.GONE);
        bottom_action_end_call.setVisibility(View.GONE);
        button2.clearColorFilter();
    }

    private void broadcasterUI(ImageView button1, ImageView button2) {
        button1.setTag(true);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                doSwitchToBroadcaster(tag == null || !((boolean) tag));
            }
        });
        button1.setColorFilter(getResources().getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = v.getTag();
                flag = true;
                if (tag != null && (boolean) tag) {
                    flag = false;
                }
                worker().getRtcEngine().muteLocalAudioStream(flag);
                button = (ImageView) v;
                button.setTag(flag);

                if (flag && muteClicked) {
                    Glide.with(LiveRoomActivity.this).load(R.drawable.ic_mic_on).into(button);


                } else if (flag) {
                    Glide.with(LiveRoomActivity.this).load(R.drawable.ic_mic_off).into(button);

                } else {
                    Glide.with(LiveRoomActivity.this).load(R.drawable.ic_mic_on).into(button);

                }
            }
        });

        button2.setVisibility(View.VISIBLE);
        bottom_action_end_call.setVisibility(View.VISIBLE);
    }

    public void onVoiceMuteClicked(View view) {
        muteClicked = true;
        RtcEngine rtcEngine = rtcEngine();
        // Stops/Resumes sending the local audio stream.
        // A successful muteLocalAudioStream method call triggers the onUserMuteAudio callback on the remote client.
        rtcEngine.muteLocalAudioStream(mAudioMuted = !mAudioMuted);

        ImageView iv = (ImageView) view;

        if (mAudioMuted) {
            Glide.with(LiveRoomActivity.this).load(R.drawable.ic_mic_off).into(iv);
        } else {
            Glide.with(LiveRoomActivity.this).load(R.drawable.ic_mic_on).into(iv);
        }
    }

    @Override
    public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
        String msg = "onJoinChannelSuccess " + channel + " " + (uid & 0xFFFFFFFFL) + " " + elapsed;
        log.debug(msg);

        notifyMessageChanged(msg);

    }

    public void onAudioVolumeIndication(IRtcEngineEventHandler.AudioVolumeInfo[] speakers, int totalVolume) {
        if (speakers.length > 0) {
            List<Integer> uidList = new ArrayList<>();
            for (IRtcEngineEventHandler.AudioVolumeInfo info : speakers) {
                if (info.volume <= 70) return;
                if (info.uid == 0)
                    uidList.add(config().mUid);
                else
                    uidList.add(info.uid);
            }
            adapterSeat.indicateSpeaking(uidList);
        }
        // Toast.makeText(this, ""+uidList.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUserOffline(int uid, int reason) {
        String msg = "onUserOffline " + (uid & 0xFFFFFFFFL) + " " + reason;
        log.debug(msg);

        notifyMessageChanged(msg);

    }


    public void onAudioChangeindicator(IRtcEngineEventHandler.AudioVolumeInfo[] speakerInfos, int totalVolume) {
        Toast.makeText(this, "sdsdsdsd", Toast.LENGTH_SHORT).show();
        onAudioVolumeIndication(speakerInfos, totalVolume);
    }

    @Override
    public void onExtraCallback(final int type, final Object... data) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isFinishing()) {
                    return;
                }

                doHandleExtraCallback(type, data);
            }
        });
    }

    private void doHandleExtraCallback(int type, Object... data) {
        int peerUid;
        boolean muted;

        switch (type) {
            case AGEventHandler.EVENT_TYPE_ON_USER_AUDIO_MUTED: {
                peerUid = (Integer) data[0];
                muted = (boolean) data[1];

                notifyMessageChanged("mute: " + (peerUid & 0xFFFFFFFFL) + " " + muted);
                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_AUDIO_QUALITY: {
                peerUid = (Integer) data[0];
                int quality = (int) data[1];
                short delay = (short) data[2];
                short lost = (short) data[3];

                notifyMessageChanged("quality: " + (peerUid & 0xFFFFFFFFL) + " " + quality + " " + delay + " " + lost);
                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_SPEAKER_STATS: {
                IRtcEngineEventHandler.AudioVolumeInfo[] infos = (IRtcEngineEventHandler.AudioVolumeInfo[]) data[0];
                onAudioVolumeIndication(infos, 1);
                if (infos.length == 1 && infos[0].uid == 0) { // local guy, ignore it
                    break;
                }

                StringBuilder volumeCache = new StringBuilder();
                for (IRtcEngineEventHandler.AudioVolumeInfo each : infos) {
                    peerUid = each.uid;
                    int peerVolume = each.volume;

                    if (peerUid == 0) {
                        continue;
                    }

                    volumeCache.append("volume: ").append(peerUid & 0xFFFFFFFFL).append(" ").append(peerVolume).append("\n");
                }

                if (volumeCache.length() > 0) {
                    String volumeMsg = volumeCache.substring(0, volumeCache.length() - 1);
                    notifyMessageChanged(volumeMsg);

                    if ((System.currentTimeMillis() / 1000) % 10 == 0) {
                        log.debug(volumeMsg);
                    }
                }
                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_APP_ERROR: {
                int subType = (int) data[0];

                if (subType == ConstantApp.AppError.NO_NETWORK_CONNECTION) {
                    showLongToast(getString(R.string.msg_no_network_connection));
                }

                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_AGORA_MEDIA_ERROR: {
                int error = (int) data[0];
                String description = (String) data[1];

                notifyMessageChanged(error + " " + description);

                break;
            }

            case AGEventHandler.EVENT_TYPE_ON_AUDIO_ROUTE_CHANGED: {
                notifyHeadsetPlugged((int) data[0]);

                break;
            }
        }
    }

    public void notifyHeadsetPlugged(final int routing) {
        mAudioRouting = routing;
        ImageView iv = findViewById(R.id.switch_speaker_id);
        if (mAudioRouting == 3) { // Speakerphone
            iv.setColorFilter(getResources().getColor(R.color.agora_blue), PorterDuff.Mode.MULTIPLY);
        } else {
            iv.clearColorFilter();
        }
    }


    public void CallAlert() {

        Dialog dialog = new Dialog(LiveRoomActivity.this);
        dialog.setContentView(R.layout.layout_custom_dialog);
        LinearLayout linearLayout = dialog.findViewById(R.id.alert_root);
        linearLayout.setMinimumWidth((int) (width * 0.8));
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.white_corner);
        dialog.setCancelable(false);

        ImageView imageView = dialog.findViewById(R.id.dialog_icon);
        imageView.setVisibility(View.VISIBLE);

        TextView title = dialog.findViewById(R.id.dialog_title);
        title.setVisibility(View.VISIBLE);
        title.setText("Welcome to Eidland");

        TextView msg = dialog.findViewById(R.id.msg);
        msg.setVisibility(View.VISIBLE);
        msg.setText("Please ensure your microphone and storage permission is given in order to get most out of Eidland");

        TextView positive = dialog.findViewById(R.id.positive_btn);
        positive.setVisibility(View.VISIBLE);
        positive.setText("Okay");
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkPermissionResult = checkSelfPermissions();
                if (checkPermissionResult) {
                    dialog.dismiss();
                } else {
                    Log.e("no permission", "Not Found");
                }
            }
        });
        dialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (StaticConfig.user != null) {
            //    Glide.with(LiveRoomActivity.this).load(StaticConfig.user.getImageurl()).into(userImage);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String strEditText = data.getStringExtra("editTextValue");
            }
        }
    }

    private boolean checkSelfPermissions() {
        return checkSelfPermission(Manifest.permission.RECORD_AUDIO, ConstantApp.PERMISSION_REQ_ID_RECORD_AUDIO) &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, ConstantApp.PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE);
    }

    public boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestCode);
            return false;
        }

        if (Manifest.permission.RECORD_AUDIO.equals(permission)) {
            ((AGApplication) getApplication()).initWorkerThread();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ConstantApp.PERMISSION_REQ_ID_RECORD_AUDIO: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, ConstantApp.PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE);
                    ((AGApplication) getApplication()).initWorkerThread();
                    Viewer viewer = new Viewer(FirebaseAuth.getInstance().getCurrentUser().getUid(), StaticConfig.user.getImageurl(), StaticConfig.user.getEmail(), StaticConfig.user.getName(), StaticConfig.user.getReceivedCoins(), config().mUid);
                    FirebaseDatabase.getInstance().getReference().child("Audiance").child(roomName).child(Clickedseat).setValue(viewer);
                    doSwitchToBroadcaster(true);
                    AgainSeat = Clickedseat;

                } else {
                    CallAlert();
                    finish();
                }
                break;
            }
            case ConstantApp.PERMISSION_REQ_ID_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Granted!");
                } else {
                    finish();
                }
                break;
            }
        }
    }

    public void gameListener() {

        FirebaseDatabase.getInstance().getReference().child("LastCard").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot currsnap) {
                if (!newlyjoined) {

                    String p = String.valueOf(currsnap.getValue());
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(p);
                    stringBuilder.append(".png");


                    StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("game_decks").child("yellow").child(stringBuilder.toString());
                    final long ONE_MEGABYTE = 1024 * 1024;
                    FirebaseStorage.getInstance().getReference();
                    storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            minimizedCard.setVisibility(View.INVISIBLE);
                            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                            displayCardImage.setImageBitmap(bmp);
                            displayCardImage.setVisibility(View.VISIBLE);

                            minimizedCard.setImageBitmap(bmp);

                            Handler showLoadingPopup = new Handler();
                            showLoadingPopup.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        cardLoadingAnimationLayout.setVisibility(View.VISIBLE);
                                        minimizedCard.setVisibility(View.INVISIBLE);
                                        cardImageURL = stringBuilder.toString();
                                    }
                                }
                            }, 300);

                            Handler endLoadingPopup = new Handler();
                            endLoadingPopup.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        if (cardImageURL != null) {
                                            cardLoadingAnimationLayout.setVisibility(View.GONE);
                                            selectedCardGIF.setVisibility(View.VISIBLE);
                                            displayCardLayout.setVisibility(View.VISIBLE);
                                            minimizedCard.setVisibility(View.VISIBLE);

                                            gameButton.setVisibility(View.GONE);
                                            seatLayout.setVisibility(View.GONE);
                                            commentBox.setVisibility(View.GONE);
                                            roomGift.setVisibility(View.GONE);

                                            Handler endCardConfetti = new Handler();
                                            endCardConfetti.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                        selectedCardGIF.setVisibility(View.INVISIBLE);
                                                    }
                                                }
                                            }, 1000);
                                        }
                                    }
                                }
                            }, 6000);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });


                } else {
                    newlyjoined = false;
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
                // ...
            }
        });


    }

    @Override
    public void onGameClick(int position, ImageView gameIcon) {

        AlertDialog.Builder gameDescription = new AlertDialog.Builder(LiveRoomActivity.this);
        if (isMod) {
            gameDescription.setTitle("Situational Cards")
                    .setMessage("what would you do if we put you in the shoes of different people? Let's hear what you'd do in certain situations!")
                    .setPositiveButton("Sure, Shuffle the Cards!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            gamesLayout.setVisibility(View.GONE);
                            // Toast.makeText(getApplicationContext(), "Moderator has shuffled cards!", Toast.LENGTH_SHORT).show();
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append((int) ((Math.random() * (19)) + 2));
                            String cardNumber = stringBuilder.toString();
                            FirebaseDatabase.getInstance().getReference("game_decks").child("yellow").child(cardNumber).child("status").setValue(Math.random());
                            cardImageURL2 = stringBuilder.toString();
                            stringBuilder.append(".png");
                            cardImageURL = stringBuilder.toString();
                            FirebaseDatabase.getInstance().getReference().child("LastCard").setValue(cardImageURL2);
                            // gameListener();

                        }
                    })
                    .setNegativeButton("I think I'll pass for now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(false)
                    .show();
        } else {
            Toast.makeText(getApplicationContext(), "you need to be a moderator", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean CheckModeratorGame(final String st) {
        FirebaseDatabase.getInstance().getReference("Mods").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                isMod = snapshot.hasChild(st);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return isMod;
    }


    @Override
    public void onViewersClick(int position, String uid, String name, String photo, String recievedCoins) {
        popup_uname.setText(name);
        clickedOnlineUserUID = uid;
        Glide.with(getApplicationContext()).load(photo).into(popup_user);
        eidlandpointcount.setText(recievedCoins);
        singleUserBox.setVisibility(View.VISIBLE);
    }
}