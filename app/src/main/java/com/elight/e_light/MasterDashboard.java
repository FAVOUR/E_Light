package com.elight.e_light;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Window;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MasterDashboard extends AppCompatActivity {


    private boolean fabExpanded = false;
    private FloatingActionButton fabSelect;
    private LinearLayout layoutFabContactUs;
    private LinearLayout layoutFabAboutUs;
    private LinearLayout layoutFabLogout;
    private View shadowView;
    private Dialog dialog;
    private FloatingActionButton customFab;
    private boolean activateSubFabs;
    private int resourceValues;
    private Window window;
    private WindowManager.LayoutParams param;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_dashboard);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        activateSubFabs = false;
        fabSelect = (FloatingActionButton) this.findViewById(R.id.fabSelect);

        layoutFabContactUs = (LinearLayout) this.findViewById(R.id.layoutFabContactUs);
        layoutFabAboutUs = (LinearLayout) this.findViewById(R.id.layoutFabAboutUs);
        layoutFabLogout = (LinearLayout) this.findViewById(R.id.layoutFabLogout);
//        shadowView = (View)findViewById(R.id.shadowView);


        //When main Fab (Settings) is activateSubFabs, it expands if not expanded already.
        //Collapses if main FAB was open already.
        //This gives FAB (Settings) open/close behavior
        fabSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded == true) {
                    closeSubMenusFab();
                } else {
                    CustomDialog();
//                    openSubMenusFab();


                }
            }
        });

        //Only main FAB is visible in the beginning
        closeSubMenusFab();



    }

    //closes FAB submenus
    private void closeSubMenusFab() {
        layoutFabContactUs.setVisibility(View.INVISIBLE);
        layoutFabAboutUs.setVisibility(View.INVISIBLE);
        layoutFabLogout.setVisibility(View.INVISIBLE);
//        shadowView.setVisibility(View.INVISIBLE);
        fabSelect.setImageResource(R.drawable.ic_settings_black_24dp);
        fabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab() {
        layoutFabContactUs.setVisibility(View.VISIBLE);
        layoutFabAboutUs.setVisibility(View.VISIBLE);
        layoutFabLogout.setVisibility(View.VISIBLE);
        shadowView.setVisibility(View.VISIBLE);


        //Change settings icon to 'X' icon
        fabSelect.setImageResource(R.drawable.ic_close_black_24dp);
        fabExpanded = true;
    }

    public void fabSetup() {
        //        setup fab button

        dialog = new Dialog(MasterDashboard.this);
        // it remove the dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

//        Change the image of the fab button
        window = dialog.getWindow();
        param = window.getAttributes();
        // set the background partial transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        window.setGravity(Gravity.CENTER);
        if (R.layout.layout_fab_submenu==resourceValues) {
            // set the layout at right bottom<br />
            param.gravity = Gravity.BOTTOM | Gravity.RIGHT;

        }

        else{

        param.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL; }

        // it dismiss the dialog when click outside the dialog frame<br />
        dialog.setCanceledOnTouchOutside(true);

    }

    public void CustomDialog() {

        resourceValues = R.layout.layout_fab_submenu;
        fabSetup();


        // set the laytout in the dialog
        dialog.setContentView(R.layout.layout_fab_submenu);

        customFab = (FloatingActionButton) dialog.findViewById(R.id.fabSelect);

        layoutFabContactUs = (LinearLayout) dialog.findViewById(R.id.layoutFabContactUs);

        layoutFabLogout = (LinearLayout) dialog.findViewById(R.id.layoutFabLogout);

        layoutFabAboutUs = (LinearLayout) dialog.findViewById(R.id.layoutFabAboutUs);


        customFab.setImageResource(R.drawable.ic_close_black_24dp);


        customFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateSubFabs = false;
                resourceValues = R.id.fabSelect;
                dialog.dismiss();
            }
        });
        dialog.show();

        layoutFabContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MasterDashboard.this, "I have been clicked \n Contact us" ,Toast.LENGTH_SHORT).show();

                activateSubFabs = true;

                resourceValues = R.id.layoutFabContactUs;
                confirm(resourceValues);
//                dialog.dismiss();
            }
        });

        layoutFabLogout.setOnClickListener(new View.OnClickListener() {

            AlertDialog.Builder builder;
            @Override
            public void onClick(View view) {
                activateSubFabs = true;

//                resourceValues = R.id.layoutFabLogout;
//
//                confirm(resourceValues);
                dialog.dismiss();
//
//                fabSetup();
//
//
//                // set the laytout in the dialog
//                dialog.setContentView(R.layout.logout);

                  builder = new AlertDialog.Builder(MasterDashboard.this);
                builder.setTitle("Confirm Logout");
                builder.setMessage("Are you sure you want to Logout?");
//                builder.setIcon(R.drawable.logo);

                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Toast.makeText(getApplicationContext(), "Pressed Yes button", Toast.LENGTH_LONG).show();

                    }
                });
                builder.setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        AuthUI.getInstance()
                                .signOut(getBaseContext())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // user is now signed out
                                        startActivity(new Intent(MasterDashboard.this, HomeActivity.class));

                                        Toast.makeText(getApplicationContext(), "Pressed No button", Toast.LENGTH_LONG).show();
                                    }
                                });



                    }
                });
                AlertDialog alert= builder.create();
                alert.show();
            }
        });

        layoutFabAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(MasterDashboard.this, "I have been clicked \n About us " ,Toast.LENGTH_SHORT).show();

                activateSubFabs = true;

                resourceValues = R.id.layoutFabAboutUs;
                confirm(resourceValues);
//                dialog.dismiss();
            }
        });
    }


    public void confirm(int resourceValues) {
        if (activateSubFabs) {
            switch (resourceValues) {

                case R.id.layoutFabContactUs:
                    dialog.dismiss();
                    fabSetup();
                    dialog.setContentView(R.layout.contact_us);
//                    LinearLayout view = (LinearLayout) dialog.findViewById(R.id.central);
//                    view.setGravity(Gravity.CENTER);

                    TextView sendMail = (TextView) dialog.findViewById(R.id.contact_email);
                    sendMail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);
                            emailIntent.setData(Uri.parse("mailto:"));
                            emailIntent.setType("text/plain");
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Re:The Enlightenment Center App");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello, There");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"TheelightCenter@gmail.com"});
//                            emailIntent.setType("message/rfc822");
                            Intent mailer = Intent.createChooser(emailIntent, "Send to ");
                            startActivity(mailer);
                            dialog.dismiss();
                        }
                    });


//                          activateSubFabs =false;
                    dialog.show();

                    break;
                case R.id.layoutFabAboutUs:
                    dialog.dismiss();
                    fabSetup();
                    dialog.setContentView(R.layout.about_us);
//                    LinearLayout view1 = (LinearLayout) dialog.findViewById(R.id.central);
//                    view1.setGravity(Gravity.CENTER);

                    Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
//                    Toast.makeText(this, "pop up! \n About us " ,Toast.LENGTH_SHORT).show();

                    dialog.show();
                    break;
                case R.id.layoutFabLogout:


            }
        }
    }

//        layoutFabContactUs = (LinearLayout) dialog.findViewById(R.id.layoutFabContactUs);
//
////         set the laytout in the dialog
//
//        customFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
////
//        dialog.show();






    public void random_quotes(View view) {
        Intent randomQuotes = new Intent(MasterDashboard.this, QuotesActivity.class);
        randomQuotes.putExtra("priority", "0");
        startActivity(randomQuotes);
    }

    public void todays_quote(View view) {
        Intent todaysQuote = new Intent(MasterDashboard.this, NotificationActivity.class);
        startActivity(todaysQuote);
    }

    public void all_Quotes(View view) {
        Intent intent = new Intent(MasterDashboard.this, QuotesActivity.class);
        intent.putExtra("priority", "1");
        startActivity(intent);
    }

    public void all_subscribers(View view) {
        Intent subscribersIntent = new Intent(MasterDashboard.this, AllSubscribers.class);

        startActivity(subscribersIntent);

    }

    public void sendMotivation(View view) {

        Intent login = new Intent(MasterDashboard.this, SendActivity.class);
        startActivity(login);

    }



}
//
//        AlertDialog dialog = new AlertDialog.Builder(context).setMessage(R.string.someText)
//        .setPositiveButton(android.R.string.ok, new OnClickListener() {
//
//@Override
//public void onClick(DialogInterface dialog, int which) {
//        dialog.dismiss();
//        // Do stuff if user accepts
//        }
//        }).setNegativeButton(android.R.string.cancel, new OnClickListener() {
//
//@Override
//public void onClick(DialogInterface dialog, int which) {
//        dialog.dismiss();
//        // Do stuff when user neglects.
//        }
//        }).setOnCancelListener(new OnCancelListener() {
//
//@Override
//public void onCancel(DialogInterface dialog) {
//        dialog.dismiss();
//        // Do stuff when cancelled
//        }
//        }).create();
//        dialog.show();