package whitepaper.smcall.fragment;

import whitepaper.smcall.R;
import whitepaper.smcall.R.id;
import whitepaper.smcall.R.layout;
import whitepaper.smcall.R.string;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
 
public class MorningCall extends Fragment implements OnTabChangeListener {
 
    private static final String TAG = "FragmentTabs";
    public static final String TAB_RANDOMCALL = "rcall";
    public static final String TAB_FRIENDSCALL = "fcall";
 
    private View mRoot;
    private TabHost mTabHost;
    private int mCurrentTab;
    
    private View leftTab;
    private View rightTab;
 
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.i(TAG, "onAttach");
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.morningcall, null, true);
        mTabHost = (TabHost) mRoot.findViewById(android.R.id.tabhost);
        setupTabs();
        
        leftTab		= mTabHost.getTabWidget().getChildAt(0);
        rightTab	= mTabHost.getTabWidget().getChildAt(1);
        
        leftTab.setBackgroundResource(R.drawable.bn);
    	rightTab.setBackgroundResource(R.drawable.te);
         
        return mRoot;
    }
 
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
 
        mTabHost.setOnTabChangedListener(this);
        mTabHost.setCurrentTab(mCurrentTab);
        // manually start loading stuff in the first tab
       
        updateTab(TAB_RANDOMCALL, R.id.tab_1);        
    }
 
    private void setupTabs() {    	
        mTabHost.setup(); // you must call this before adding your tabs!    
        mTabHost.addTab(newTab(TAB_RANDOMCALL, R.string.tab_randomcall, R.id.tab_1));
        mTabHost.addTab(newTab(TAB_FRIENDSCALL, R.string.tab_friendscall, R.id.tab_2));        
    }
 
    private TabSpec newTab(String tag, int labelId, int tabContentId) {
        Log.d(TAG, "buildTab(): tag=" + tag);
 
        /*
        View indicator = LayoutInflater.from(getActivity()).inflate(R.layout.tab,
                (ViewGroup) mRoot.findViewById(android.R.id.tabs), false);
        ((TextView) indicator.findViewById(R.id.text)).setText(labelId);
        */
 
        TabSpec tabSpec = mTabHost.newTabSpec(tag);
        //tabSpec.setIndicator(String.valueOf(labelId));
        tabSpec.setIndicator("");
        tabSpec.setContent(tabContentId);
        return tabSpec;
    }
 
    @Override
    public void onTabChanged(String tabId) {
        Log.d(TAG, "onTabChanged(): tabId=" + tabId);
        if (TAB_RANDOMCALL.equals(tabId)) {
        	
        	leftTab.setBackgroundResource(R.drawable.bn);
        	rightTab.setBackgroundResource(R.drawable.te);
        	
            updateTab(tabId, R.id.tab_1);
                                               
            mCurrentTab = 0;
            return;
        }
        if (TAB_FRIENDSCALL.equals(tabId)) {
            updateTab(tabId, R.id.tab_2);
                        
            leftTab.setBackgroundResource(R.drawable.te);
        	rightTab.setBackgroundResource(R.drawable.bn);
            
            mCurrentTab = 1;
            return;
        }
    }
    
    
 
    private void updateTab(String tabId, int placeholder) {
        FragmentManager fm = getFragmentManager();
        
        if (fm.findFragmentByTag(tabId) == null) {
        	if(tabId == TAB_RANDOMCALL){        		
        		fm.beginTransaction()        		
                .replace(placeholder, new RandomCallFrg(), tabId)
                .commit();        	
        	}else if(tabId == TAB_FRIENDSCALL){
        		fm.beginTransaction()
                .replace(placeholder, new RecordFrag(), tabId)
                .commit();
        	}        	
        }        
    }
    
    
 
}