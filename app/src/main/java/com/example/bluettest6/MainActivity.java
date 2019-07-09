package com.example.bluettest6;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity {

    TextView ondo;
    TextView num1,num2;

    // 블루투스 변수생성
    private BluetoothSPP bt;

    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        ondo = (TextView)findViewById(R.id.ondo);
        ondo.setText("\u2103");

        num1 = (TextView)findViewById(R.id.num1);
        num2 = (TextView)findViewById(R.id.num2);

        // bt 블루투스 변수 생성자
        bt = new BluetoothSPP(this);

        // 만약 블루투스 이용할수 없으면
        if (!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
            // 앱 종료
        }

        // 아두이노로부터 수신한 데이터 세팅해주는 메서드 >> 우리는 사용 안함
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                // message 변수에 데이터 담아와서 토스트 메시지 띄우기
                num1.setText(message.substring(0,2));
                num2.setText(message.substring(2,4));
                //Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        // 블루투스 연결 완료되면
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                // 어떤 기기가 연결되었는지  토스트 메시지 띄우기
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            // 블루투스 연결 해제되면
            public void onDeviceDisconnected() {
                // 연결 해제 토스트 메시지 띄우기
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            // 블루투스 연결 실패하면
            public void onDeviceConnectionFailed() {
                // 이용 불가 토스트 메시지 띄우기
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

        // 블루투스 연결버튼 변수 선언
        ImageButton btnConnect = findViewById(R.id.btnConnect);
        // 연결 버튼 클릭 리스너
        // 클릭 시, 페어링 되어있는 기기 목록 띄우는 화면 생성
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });
    }

    // 블루투스 중지 메서드 선언
    public void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    // 블루투스 시작 메서드 선언
    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                //DEVICE_ANDROID는 안드로이드 기기 끼리
                setup();
            }
        }
    }

    // 블루투스 연결 완료하고 사용할 준비가되면 자동으로 실행되는 메서드 (onStart() 메서드에서 마지막에 호출해줌)
    public void setup() {
        final Handler mHandler = new Handler();
        final LinearLayout play = (LinearLayout) findViewById(R.id.play);

        // 종료 버튼 변수 선언
        Button btnOff = findViewById(R.id.btnOff);
        // 종료 버튼 클릭 리스너
        btnOff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 클릭하자마자 다이얼로그 띄워서 질문
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this
                );
                alert.setTitle("떠나시는군요 ㅠㅠ");
                alert.setMessage("주인님! 모든 작동을 종료할까요 ^0^?").setCancelable(false)
                        .setPositiveButton("응", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // 클릭하면 아두이노로 0 데이터 송신
                                bt.send("0", true);
                                // 이전에 진행중이던 예약 전부 지우기
                                mHandler.removeMessages(0);

                                // 작동중이라는 텍스트 없애기
                                if (play.getVisibility() == View.VISIBLE) {
                                    play.setVisibility(View.INVISIBLE);
                                }

                            }
                        }).setNegativeButton("아니", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();

            }
        });
// 그냥 올려봄

        // 수면-강 버튼 클릭 리스너
        Button btnStrong_1 = findViewById(R.id.strong_1);
        btnStrong_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // 클릭하자마자 다이얼로그 띄워서 질문
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this
                );
                alert.setTitle("피곤하시군요??");
                // 시간 설정하는 에디트텍스트 (입력)
                final EditText et = new EditText(MainActivity.this);
                et.setPadding(10, 10, 10, 10);
                //et.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(et);
                alert.setMessage("주인님! 수면에 좋은 향을 몇분간 작동 할까요 ^0^?").setCancelable(false)
                        .setPositiveButton("응", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 에디트테그트에 입력되어있는 시간을 실수형으로 형변환
                                Float numInt = Float.parseFloat(et.getText().toString());

                                //이전에 진행중이던 작동 멈추기
                                mHandler.removeMessages(0);
                                bt.send("0", true);

                                // 클릭하면 아두이노로 1 데이터 송신
                                bt.send("1", true);
                                if (play.getVisibility() == View.INVISIBLE) {
                                    play.setVisibility(View.VISIBLE);
                                }
                                mHandler.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        // 아래의 딜레이 이후 아두이노로 0 데이터 송신하기
                                        bt.send("0", true);
                                        if (play.getVisibility() == View.VISIBLE) {
                                            play.setVisibility(View.INVISIBLE);
                                        }

                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, (int)(numInt*1000)); // 분 단위로 예약걸기
                            }
                        }).setNegativeButton("아니", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();


            }
        });


        // 1번 약 버튼 클릭 리스너
        Button btnWeak_1 = findViewById(R.id.weak_1); //데이터 전송
        btnWeak_1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this
                );
                alert.setTitle("피곤하시군요??");
                final EditText et = new EditText(MainActivity.this);
                et.setPadding(10, 10, 10, 10);
                //et.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(et);
                alert.setMessage("주인님! 수면에 좋은 향을 몇분간 작동 할까요 ^0^?").setCancelable(false)
                        .setPositiveButton("응", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Float numInt = Float.parseFloat(et.getText().toString());

                                //클릭하면 아두이노로 1 데이터 송신, 1번 강 on 토스트메시지 띄우기
                                mHandler.removeMessages(0);
                                bt.send("0", true);
                                bt.send("2", true);
                                if (play.getVisibility() == View.INVISIBLE) {
                                    play.setVisibility(View.VISIBLE);
                                }
                                mHandler.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        bt.send("0", true);
                                        if (play.getVisibility() == View.VISIBLE) {
                                            play.setVisibility(View.INVISIBLE);
                                        }

                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, (int)(numInt*1000));// 0.5초 정도 딜레이를 준 후 시작

                            }
                        }).setNegativeButton("아니", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();



            }
        });

        // 2번 강 버튼 클릭 리스너
        Button btnStrong_2 = findViewById(R.id.strong_2); //데이터 전송
        btnStrong_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this
                );
                alert.setTitle("집중하세요 !");
                final EditText et = new EditText(MainActivity.this);
                et.setPadding(10, 10, 10, 10);
                //et.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(et);
                alert.setMessage("주인님! 집중에 좋은 향을 몇분간 작동 할까요 ^0^?").setCancelable(false)
                        .setPositiveButton("응", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Float numInt = Float.parseFloat(et.getText().toString());

                                // 클릭하면 아두이노로 1 데이터 송신, 1번 강 on 토스트메시지 띄우기
                                mHandler.removeMessages(0);
                                bt.send("0", true);
                                bt.send("3", true);
                                if (play.getVisibility() == View.INVISIBLE) {
                                    play.setVisibility(View.VISIBLE);
                                }
                                mHandler.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        bt.send("0", true);
                                        if (play.getVisibility() == View.VISIBLE) {
                                            play.setVisibility(View.INVISIBLE);
                                        }

                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, (int)(numInt*1000));// 0.5초 정도 딜레이를 준 후 시작

                            }
                        }).setNegativeButton("아니", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();



            }
        });

        // 2번 약 버튼 클릭 리스너
        Button btnWeak_2 = findViewById(R.id.weak_2); //데이터 전송
        btnWeak_2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this
                );
                alert.setTitle("집중하세요 !");
                final EditText et = new EditText(MainActivity.this);
                et.setPadding(10, 10, 10, 10);
                //et.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(et);
                alert.setMessage("주인님! 집중에 좋은 향을 몇분간 작동 할까요 ^0^?").setCancelable(false)
                        .setPositiveButton("응", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Float numInt = Float.parseFloat(et.getText().toString());

                                // 클릭하면 아두이노로 1 데이터 송신, 1번 강 on 토스트메시지 띄우기
                                mHandler.removeMessages(0);
                                bt.send("0", true);
                                bt.send("4", true);
                                if (play.getVisibility() == View.INVISIBLE) {
                                    play.setVisibility(View.VISIBLE);
                                }
                                mHandler.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        bt.send("0", true);
                                        if (play.getVisibility() == View.VISIBLE) {
                                            play.setVisibility(View.INVISIBLE);
                                        }

                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, (int)(numInt*1000));// 0.5초 정도 딜레이를 준 후 시작

                            }
                        }).setNegativeButton("아니", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        // 3번 강 버튼 클릭 리스너
        Button btnStrong_3 = findViewById(R.id.strong_3); //데이터 전송
        btnStrong_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this
                );
                alert.setTitle("요즘 날씨가 많이 건조하죠 ㅠㅠ");
                final EditText et = new EditText(MainActivity.this);
                et.setPadding(10, 10, 10, 10);
                //et.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(et);
                alert.setMessage("주인님! 가습기를 몇분간 작동 할까요 ^0^?").setCancelable(false)
                        .setPositiveButton("응", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Float numInt = Float.parseFloat(et.getText().toString());

                                // 클릭하면 아두이노로 1 데이터 송신, 1번 강 on 토스트메시지 띄우기
                                mHandler.removeMessages(0);
                                bt.send("0", true);
                                bt.send("5", true);
                                if (play.getVisibility() == View.INVISIBLE) {
                                    play.setVisibility(View.VISIBLE);
                                }
                                mHandler.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        bt.send("0", true);
                                        if (play.getVisibility() == View.VISIBLE) {
                                            play.setVisibility(View.INVISIBLE);
                                        }

                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, (int)(numInt*1000));// 0.5초 정도 딜레이를 준 후 시작

                            }
                        }).setNegativeButton("아니", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();

            }
        });

        // 3번 약 버튼 클릭 리스너
        Button btnWeak_3 = findViewById(R.id.weak_3); //데이터 전송
        btnWeak_3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this
                );
                alert.setTitle("요즘 날씨가 많이 건조하죠 ㅠㅠ");
                final EditText et = new EditText(MainActivity.this);
                et.setPadding(10, 10, 10, 10);
                //et.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(et);
                alert.setMessage("주인님! 가습기를 몇분간 작동 할까요 ^0^?").setCancelable(false)
                        .setPositiveButton("응", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Float numInt = Float.parseFloat(et.getText().toString());

                                // 클릭하면 아두이노로 1 데이터 송신, 1번 강 on 토스트메시지 띄우기
                                mHandler.removeMessages(0);
                                bt.send("0", true);
                                bt.send("6", true);
                                if (play.getVisibility() == View.INVISIBLE) {
                                    play.setVisibility(View.VISIBLE);
                                }
                                mHandler.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        bt.send("0", true);
                                        if (play.getVisibility() == View.VISIBLE) {
                                            play.setVisibility(View.INVISIBLE);
                                        }

                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, (int)(numInt*1000));// 0.5초 정도 딜레이를 준 후 시작
                            }
                        }).setNegativeButton("아니", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();



            }
        });

        // 믹스 버튼 생성
        Button mix = findViewById(R.id.mix);
        mix.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this
                );
                alert.setTitle("MIXED");
                final EditText et = new EditText(MainActivity.this);
                et.setPadding(10, 10, 10, 10);
                //et.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(et);
                alert.setMessage("주인님! MIX를 몇분간 작동 할까요 ^0^?").setCancelable(false)
                        .setPositiveButton("응", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Float numInt = Float.parseFloat(et.getText().toString());

                                // 클릭하면 아두이노로 1 데이터 송신, 1번 강 on 토스트메시지 띄우기
                                mHandler.removeMessages(0);
                                bt.send("0", true);
                                bt.send("7", true);
                                if (play.getVisibility() == View.INVISIBLE) {
                                    play.setVisibility(View.VISIBLE);
                                }
                                mHandler.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        bt.send("0", true);
                                        if (play.getVisibility() == View.VISIBLE) {
                                            play.setVisibility(View.INVISIBLE);
                                        }

                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, (int)(numInt*1000));// 0.5초 정도 딜레이를 준 후 시작

                            }
                        }).setNegativeButton("아니", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        // 믹스 버튼2 생성
        Button mix2 = findViewById(R.id.mix2);
        mix2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this
                );
                alert.setTitle("수면+가습");
                final EditText et = new EditText(MainActivity.this);
                et.setPadding(10, 10, 10, 10);
                //et.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(et);
                alert.setMessage("주인님! (수면+가습)을 몇분간 작동 할까요 ^0^?").setCancelable(false)
                        .setPositiveButton("응", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Float numInt = Float.parseFloat(et.getText().toString());

                                // 클릭하면 아두이노로 1 데이터 송신, 1번 강 on 토스트메시지 띄우기
                                mHandler.removeMessages(0);
                                bt.send("0", true);
                                bt.send("8", true);
                                if (play.getVisibility() == View.INVISIBLE) {
                                    play.setVisibility(View.VISIBLE);
                                }
                                mHandler.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        bt.send("0", true);
                                        if (play.getVisibility() == View.VISIBLE) {
                                            play.setVisibility(View.INVISIBLE);
                                        }

                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, (int)(numInt*1000));// 0.5초 정도 딜레이를 준 후 시작

                            }
                        }).setNegativeButton("아니", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });

        // 믹스 버튼3 생성
        Button mix3 = findViewById(R.id.mix3);
        mix3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this
                );
                alert.setTitle("집중+가습");
                final EditText et = new EditText(MainActivity.this);
                et.setPadding(10, 10, 10, 10);
                //et.setInputType(InputType.TYPE_CLASS_NUMBER);
                alert.setView(et);
                alert.setMessage("주인님! (집중+가습)을 몇분간 작동 할까요 ^0^?").setCancelable(false)
                        .setPositiveButton("응", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Float numInt = Float.parseFloat(et.getText().toString());

                                // 클릭하면 아두이노로 1 데이터 송신, 1번 강 on 토스트메시지 띄우기
                                mHandler.removeMessages(0);
                                bt.send("0", true);
                                bt.send("9", true);
                                if (play.getVisibility() == View.INVISIBLE) {
                                    play.setVisibility(View.VISIBLE);
                                }
                                mHandler.postDelayed(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        bt.send("0", true);
                                        if (play.getVisibility() == View.VISIBLE) {
                                            play.setVisibility(View.INVISIBLE);
                                        }

                                        //여기에 딜레이 후 시작할 작업들을 입력
                                    }
                                }, (int)(numInt*1000));// 0.5초 정도 딜레이를 준 후 시작

                            }
                        }).setNegativeButton("아니", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = alert.create();
                alertDialog.show();
            }
        });


    }

    // 아두이노에서 앱으로 데이터 수신시 발생되는 메서드 >> 우리는 사용안함
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
