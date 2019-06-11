package com.example.asus.appoitment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Appointment extends AppCompatActivity {
    String temp;
    int len;
    String[] appointment;
    RelativeLayout parentlayout;
    CardView cardview;
    RelativeLayout.LayoutParams layoutparams;
    TextView textview;
    Context c;
    Button yes,no,yesaccept,noaccept;
    ArrayList<String> appoinmentlist = new ArrayList<>();
    ArrayList<ExampleItem> exampleList = new ArrayList<>();
    PopupWindow popupWindow;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ExampleItem> mExampleList;

    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;

    private Button accept;
    private Button reject;
    String ip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        ip = bundle.getString("ip");
        setTitle("Appointment Requests ");
        c = getApplicationContext();
        parentlayout = findViewById(R.id.parentlayout);
        HashMap<String, String> getData = new HashMap<String, String>();
        getData.put("head","Ankit_SOS");
        PostResponseAsyncTask task2 = new PostResponseAsyncTask(Appointment.this, getData, new AsyncResponse() {
            @Override
            public void processFinish(String s) {
                if (!(s.isEmpty())) {
                    //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong! Try later!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        try {
            temp = task2.execute("http://"+ip+"/appointment_scripts/HEAD_PENDING_SCHEDULE.php").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        task2.setEachExceptionsHandler(new EachExceptionsHandler() {
            @Override
            public void handleIOException(IOException e) {
                Toast.makeText(getApplicationContext(), "Cannot connect to server!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void handleMalformedURLException(MalformedURLException e) {
                Toast.makeText(getApplicationContext(), "URL Error!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void handleProtocolException(ProtocolException e) {
                Toast.makeText(getApplicationContext(), "Protocol Error!", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                Toast.makeText(getApplicationContext(), "Encoding Error!", Toast.LENGTH_SHORT).show();

            }
        });
        appointment = temp.split(";");
        len = appointment.length;
        createExampleList();
        buildRecyclerView();
        //setButtons();

    }
//    public void insertItem(int position) {
//        mExampleList.add(position, new ExampleItem(R.drawable.ic_android, "New Item At Position" + position, "This is Line 2"));
//        mAdapter.notifyItemInserted(position);
//    }

    public void removeItem(int position) {
        mExampleList.remove(position);
        mAdapter.notifyItemRemoved(position);
    }

//    public void changeItem(int position, String text) {
//        mExampleList.get(position).changeText1(text);
//        mAdapter.notifyItemChanged(position);
//    }

    public void createExampleList() {
        mExampleList = new ArrayList<>();
        for(int i = 0;i < len ; i++) {
            String[] local;
            local = appointment[i].split(",");
            appoinmentlist.add(appointment[i]);
            mExampleList.add(new ExampleItem(local[0]+","+local[1]+","+local[2]));
        }
    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                Intent intent = new Intent(Appointment.this,Information.class);
                intent.putExtra("info",appoinmentlist.get(position));
                startActivity(intent);
            }

            @Override
            public void onAcceptClick(final int position) {
                LayoutInflater layoutInflater = (LayoutInflater) Appointment.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.popupaccept, null);
                yesaccept = customView.findViewById(R.id.yesaccept);
                noaccept = customView.findViewById(R.id.noaccept);
                popupWindow = new PopupWindow(customView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(600);
                //display the popup window
                popupWindow.showAtLocation(parentlayout, Gravity.CENTER, 0, 0);
                popupWindow.setFocusable(true);
                popupWindow.update();
                yesaccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeItem(position);
                        popupWindow.dismiss();
                        HashMap<String, String> getData = new HashMap<String, String>();
                        String[] local;
                        local = appointment[position].split(",");

                        getData.put("head","Ankit_SOS");
                        getData.put("officer",local[0]);
                        getData.put("topic",local[1]);
                        getData.put("date",local[2]);
                        getData.put("time",local[3]);
                        getData.put("attendees",local[4]);
                        getData.put("pending","Approved");
                        PostResponseAsyncTask task2 = new PostResponseAsyncTask(Appointment.this, getData, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                if (!(s.isEmpty())) {
                                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Something went wrong! Try later!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        try {
                            temp = task2.execute("http://"+ip+"/appointment_scripts/HEAD_EDIT_STATUS.php").get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        task2.setEachExceptionsHandler(new EachExceptionsHandler() {
                            @Override
                            public void handleIOException(IOException e) {
                                Toast.makeText(getApplicationContext(), "Cannot connect to server!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleMalformedURLException(MalformedURLException e) {
                                Toast.makeText(getApplicationContext(), "URL Error!", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void handleProtocolException(ProtocolException e) {
                                Toast.makeText(getApplicationContext(), "Protocol Error!", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                                Toast.makeText(getApplicationContext(), "Encoding Error!", Toast.LENGTH_SHORT).show();

                            }
                        });
                        appoinmentlist.remove(position);

                    }
                });
                noaccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }

            @Override
            public void onRejectClick(final int position) {
                LayoutInflater layoutInflater = (LayoutInflater) Appointment.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.popup, null);
                yes = customView.findViewById(R.id.yes);
                no = customView.findViewById(R.id.no);
                popupWindow = new PopupWindow(customView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(600);
                //display the popup window
                popupWindow.showAtLocation(parentlayout, Gravity.CENTER, 0, 0);
                popupWindow.setFocusable(true);
                popupWindow.update();
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeItem(position);
                        popupWindow.dismiss();
                        HashMap<String, String> getData = new HashMap<String, String>();
                        String[] local;
                        local = appointment[position].split(",");

                        getData.put("head","Ankit_SOS");
                        getData.put("officer",local[0]);
                        getData.put("topic",local[1]);
                        getData.put("date",local[2]);
                        getData.put("time",local[3]);
                        getData.put("attendees",local[4]);
                        getData.put("pending","Declined");
                        PostResponseAsyncTask task2 = new PostResponseAsyncTask(Appointment.this, getData, new AsyncResponse() {
                            @Override
                            public void processFinish(String s) {
                                if (!(s.isEmpty())) {
                                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Something went wrong! Try later!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
                        try {
                            temp = task2.execute("http://"+ip+"/appointment_scripts/HEAD_EDIT_STATUS.php").get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        task2.setEachExceptionsHandler(new EachExceptionsHandler() {
                            @Override
                            public void handleIOException(IOException e) {
                                Toast.makeText(getApplicationContext(), "Cannot connect to server!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void handleMalformedURLException(MalformedURLException e) {
                                Toast.makeText(getApplicationContext(), "URL Error!", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void handleProtocolException(ProtocolException e) {
                                Toast.makeText(getApplicationContext(), "Protocol Error!", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                                Toast.makeText(getApplicationContext(), "Encoding Error!", Toast.LENGTH_SHORT).show();

                            }
                        });
                        appoinmentlist.remove(position);

                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });
            }
        });
    }

//    public void setButtons() {
//        buttonInsert = findViewById(R.id.button_insert);
//        buttonRemove = findViewById(R.id.button_remove);
//        editTextInsert = findViewById(R.id.edittext_insert);
//        editTextRemove = findViewById(R.id.edittext_remove);
//
//        buttonInsert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = Integer.parseInt(editTextInsert.getText().toString());
//                insertItem(position);
//            }
//        });
//
//        buttonRemove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int position = Integer.parseInt(editTextRemove.getText().toString());
//                removeItem(position);
//            }
//        });
//    }
}
