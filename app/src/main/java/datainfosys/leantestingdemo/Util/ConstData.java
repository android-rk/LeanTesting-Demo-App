package datainfosys.leantestingdemo.Util;

import java.util.ArrayList;

/**
 * Created by Data on 6/9/2016.
 */
public class ConstData {
    public static final String BASEURL = "https://api.leantesting.com/v1/";
    public static final String ENDPOINT = "?access_token=";
    //    getitng projects list
    public static final String PROJECTS = "projects";
    public static final String SECTIONS = "sections";
    public static final String VERSIONS="versions";
    public static final String PAGE="&page=";
    //    getting bugs list
    public static final String BUGS = "bugs";
    public static final String[] STATUS = {"New", "Confirmed", "Feedback", "Resolved", "Acknowledged", "Closed", "In Progress"};
    public static final String[] BUG_SERVITY = {"Minor", "Major", "Critical", "Trivial"};
    public static final ArrayList<String> PROJECT_VERSION = new ArrayList<>();
    public static ArrayList<Integer> VERSIONS_DATA = new ArrayList<>();
    public static final String[] BUG_PRIORITY = {"Low", "Medium", "High", "Critical"};
    public static final String[] BUG_REPRODUCIBILATY = {"Always", "Sometimes", "Random", "Once"};
    public static final String[] BUG_TYPE = {"Functional", "Usability", "Text", "Visual", "User_input_data", "Suggestion"};
    public static ArrayList<Integer> BUG_SECTIONS = new ArrayList<>();
    public static ArrayList<String> BUG_SECTIONS_STRING = new ArrayList<>();
}
