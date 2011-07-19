package com.dougsko.diceware.test;

import com.dougsko.diceware.Diceware;
import android.test.ActivityInstrumentationTestCase2;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class DicewareTest extends ActivityInstrumentationTestCase2<Diceware> {
	private Diceware mActivity;
	private TextView mView;
	private TextView mOutput;
	private String resourceString;
	private Spinner mSpinner;
	private SpinnerAdapter mModeData;
	private int mPos;
	private String mSelection;
	private Button mButton1;
	private Button mButton5;
	private Button mButton6;
	private Button mRandomOrg;
	private Button mCopyToClip;
	private Button mClear;
	private ClipboardManager mClipBoard;
	
	public DicewareTest() {
		super("com.dougsko.diceware", Diceware.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mActivity = this.getActivity();
		mView = (TextView) mActivity.findViewById(com.dougsko.diceware.R.id.output_label);
		resourceString = mActivity.getString(com.dougsko.diceware.R.string.output_label);
		mOutput = (TextView) mActivity.findViewById(com.dougsko.diceware.R.id.output);
		mSpinner = (Spinner) mActivity.findViewById(com.dougsko.diceware.R.id.spinner);
		mModeData = mSpinner.getAdapter();
		mButton1 = (Button) mActivity.findViewById(com.dougsko.diceware.R.id.one);
		mButton5 = (Button) mActivity.findViewById(com.dougsko.diceware.R.id.five);
		mButton6 = (Button) mActivity.findViewById(com.dougsko.diceware.R.id.six);
		mRandomOrg = (Button) mActivity.findViewById(com.dougsko.diceware.R.id.randomOrg);
		mCopyToClip = (Button) mActivity.findViewById(com.dougsko.diceware.R.id.copy_to_clipboard);
		mClear = (Button) mActivity.findViewById(com.dougsko.diceware.R.id.clear);
	}
	
	public void testPreconditions() {
		assertNotNull(mView);
		assertTrue(mSpinner.getOnItemSelectedListener() != null);
		assertTrue(mModeData != null);
		assertEquals(mModeData.getCount(), 4);
	}
	
	public void testText() {
		assertEquals(resourceString, mView.getText());
	}
	
	public void testSpinnerUI() {
		mActivity.runOnUiThread(
			new Runnable() {
				public void run() {
					mSpinner.requestFocus();
					mSpinner.setSelection(0);
				}
			}
		);
		this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		for (int i = 1; i <= 3; i++) {
			this.sendKeys(KeyEvent.KEYCODE_DPAD_DOWN);
		}
		this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		
		mPos = mSpinner.getSelectedItemPosition();
		mSelection = (String) mSpinner.getItemAtPosition(mPos);
		
		assertEquals(mSelection, "Numbers");
	}
	
	public void testWords() {
		mActivity.runOnUiThread(
			new Runnable() {
				public void run() {
					mSpinner.requestFocus();
					mSpinner.setSelection(0);
					mButton1.requestFocus();
				}
			}
		);
		for (int i = 0; i <= 4; i++) {
			this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		}
		assertEquals(mOutput.getText().toString(), " a ");
	}
	
	public void testAscii() {
		mActivity.runOnUiThread(
			new Runnable() {
				public void run() {
					mSpinner.requestFocus();
					mSpinner.setSelection(1);
					mButton1.requestFocus();
				}
			}
		);
		for (int i = 0; i <= 2; i++) {
			this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		}
		assertEquals(mOutput.getText().toString(), " A ");
	}
	
	// switch to alphanumeric
	// press 1165
	// should equal " A 3 "
	public void testAlphanumeric() {
		mActivity.runOnUiThread(
				new Runnable() {
					public void run() {
						mSpinner.requestFocus();
						mSpinner.setSelection(2);
						mButton1.requestFocus();
					}
				}
			);
			for (int i = 0; i <= 1; i++) {
				this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
			}
		mActivity.runOnUiThread(
				new Runnable() {
					public void run() {
						mButton6.requestFocus();
					}
				}
			);
			this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		mActivity.runOnUiThread(
				new Runnable() {
					public void run() {
						mButton5.requestFocus();
					}
				}
			);
			this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
			Log.d("DICEWARE TEST", mOutput.getText().toString());
			assertEquals(mOutput.getText().toString(), " A 3 ");
	}
	
	public void testNumbers() {
		mActivity.runOnUiThread(
			new Runnable() {
				public void run() {
					mSpinner.requestFocus();
					mSpinner.setSelection(3);
					mButton1.requestFocus();
				}
			}
		);
		for (int i = 0; i <= 1; i++) {
			this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		}
		mActivity.runOnUiThread(
			new Runnable() {
				public void run() {
					mButton6.requestFocus();
				}
			}
		);
		this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		assertEquals(mOutput.getText().toString(), " 1 ");
	}
	
	
	public void testRandomOrg() {
		mActivity.runOnUiThread(
			new Runnable() {
				public void run() {
					mSpinner.requestFocus();
					mSpinner.setSelection(0);
					mRandomOrg.requestFocus();
				}
			}
		);
		this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		CharSequence phrase = (CharSequence) mOutput.getText();
		assertFalse(phrase.toString().subSequence(0, phrase.length()-1).equals(" "));
	}
	
	public void testClipBoard() {
		mActivity.runOnUiThread(
			new Runnable() {
				public void run() {
					mButton1.requestFocus();
				}
			}
		);
		for (int i = 0; i <= 4; i++) {
			this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		}
		mActivity.runOnUiThread(
			new Runnable() {
				public void run() {
					mClipBoard = (ClipboardManager) mActivity.getSystemService(android.content.Context.CLIPBOARD_SERVICE); 
					mCopyToClip.requestFocus();
				}
			}
		);
		this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		assertEquals(mClipBoard.getText().toString(), "a");
	}
	
	public void testClearButton() {
		mActivity.runOnUiThread(
			new Runnable() {
				public void run() {
					mRandomOrg.requestFocus();
				}
			}
		);
		this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		mActivity.runOnUiThread(
			new Runnable() {
				public void run() {
					mClear.requestFocus();
				}
			}
		);
		this.sendKeys(KeyEvent.KEYCODE_DPAD_CENTER);
		assertEquals(mOutput.getText().toString(), "");
	}
}
