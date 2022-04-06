package com.app.myplant.identify_image;

import com.google.firebase.storage.StorageException;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ImageIdenifyResponse {
    @SerializedName("suggestions")
    public ArrayList<Suggestion> suggestions;



    public static class Suggestion{
        @SerializedName("plant_name")
        public String plant_name;

        @SerializedName("probability")
        public String probability;

        @SerializedName("plant_details")
        public PlantDetails plantDetails;



        public static class PlantDetails{
            @SerializedName("wiki_image")
            public WikiValue wikiImage;

            @SerializedName("wiki_description")
            public WikiValue wikiDescription;




            public static class WikiValue{
                @SerializedName("value")
                public String value;


                public String getValue() {
                    return value;
                }
            }

            public WikiValue getWikiImage() {
                return wikiImage;
            }

            public WikiValue getWikiDescription() {
                return wikiDescription;
            }
        }


        public String getPlant_name() {
            return plant_name;
        }

        public String getProbability() {
            return probability;
        }

        public PlantDetails getPlantDetails() {
            return plantDetails;
        }
    }

    public ArrayList<Suggestion> getSuggestions() {
        return suggestions;
    }
}
