package com.hubert.coronavirusUpdate.ui.about;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AboutViewModel extends ViewModel {

    private MutableLiveData<String> mText,mText1,mText2,mText3,
            mText4,mText5,mText6,mText7,mText8,mText9;
    private static final String ABOUTVirus = "What are Coronavirus";
    private static final String dataOnVirus = "Coronavirus are a large grorup of viruses" +
            " that are common among animals. According to the US Centers for Disease Control" +
            " and Prevention, it is a dangerous disease with incubation period between 4-6 days.";

    private static final String spread = "How it spreads";
    private static final String howSpread = "The Virus can be spread from animals to " +
            " and from human to human. Human to Human transmission exposure factors are; \n" +
            "- A cough \n- Sneeze \n- Handshake";

    private static final String hSigns = "Signs and Symptoms";
    private static final String mSigns = "- fever \n- Running nose \n- cough \n- sore throat " +
            "\n- headache";

    private static final String hPrevent = "Prevention";
    private static final String mPrevent ="- Avoid touching face with unwashed Hands " +
            "\n- Cover your mouth and nose when sneezing or coughing " +
            "\n- Frequency use hand Sanitizers after touching objects or surfaces " +
            "\n- If you sick, stay home and avoid crowds and contact with others. ";

//    private static final String hTreatment = "Treatment";
//    private static final String mTreatment ="- No specific treatment has come up yet. " +
//            "\n- Self quarantine and call or visit the COVID-19 center immediately" +
//            "\n- Pain and fever medications are prescribed to relieve symptoms. " +
//            "\n- Drink a lot of fluids, specifically water " +
//            "\n- Get plenty of rest " +
//            "\n- If symptoms feels worse, see a doctor. ";



    public AboutViewModel() {
        mText = new MutableLiveData<>();
        mText1 = new MutableLiveData<>();
        mText2 = new MutableLiveData<>();
        mText3 = new MutableLiveData<>();
        mText4 = new MutableLiveData<>();
        mText5 = new MutableLiveData<>();
        mText6 = new MutableLiveData<>();
        mText7 = new MutableLiveData<>();
        mText8 = new MutableLiveData<>();
        mText9 = new MutableLiveData<>();

        mText.setValue(ABOUTVirus);
        mText1.setValue(dataOnVirus);
        mText2.setValue(spread);
        mText3.setValue(howSpread);
        mText4.setValue(hSigns);
        mText5.setValue(mSigns);
        mText6.setValue(hPrevent);
        mText7.setValue(mPrevent);
//        mText8.setValue(hTreatment);
//        mText9.setValue(mTreatment);
    }

    public LiveData<String> getTextAbout() {
        return mText;
    }
    public LiveData<String> getDataOnVirus() {
        return mText1;
    }
    public LiveData<String> getSpreadVirus() {
        return mText2;
    }
    public LiveData<String> getHowVirusSpread() {
        return mText3;
    }
    public LiveData<String> getHSigns() {
        return mText4;
    }
    public LiveData<String> getMSigns() {
        return mText5;
    }
    public LiveData<String> getHPrevent() {
        return mText6;
    }
    public LiveData<String> getMPrevent() {
        return mText7;
    }
//    public LiveData<String> getHTreatment() {
//        return mText8;
//    }
//    public LiveData<String> getMTreatment() {
//        return mText9;
//    }





}