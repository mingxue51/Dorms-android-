package entity.Property.Search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Suggestions {
    public static final int CITY = 1;
    public static final int PROPERTY = 2;
    public static final int DISTRICT = 3;
    public static final int LANDMARK = 4;
    //public static final int SEPARATOR = 3;
    private List<Suggestion> suggestions;

    private Decorator decorator;
    private SectionedList sectionedList;

    public Suggestions() {
        suggestions = new ArrayList<Suggestion>();
        decorator = new Decorator();
        sectionedList = new SectionedList();
    }
    public Suggestions(List<Suggestion> pSuggestions) {
        suggestions = pSuggestions;
        decorator = new Decorator();
        sectionedList = new SectionedList();
    }


    public List<Suggestion> getSuggestions() {
        return suggestions;
    }

    public Suggestions setSuggestions(List<Suggestion> suggestions) {
        this.suggestions = suggestions;
        return this;
    }

    public Decorator getDecorator() {
        return decorator;
    }

    public Suggestions setDecorator(Decorator decorator) {
        this.decorator = decorator;
        return this;
    }

    protected class Decorator implements Serializable {
        private static final long serialVersionUID = 1L;

        protected int type;

        public int getType() {
            return type;
        }

        public Decorator setType(int type) {
            this.type = type;
            return this;
        }

        public boolean isProperty() {
            return false;
        }

        public boolean isCity() {
            return false;
        }
    }


    public SectionedList getSectionedList() {
        return sectionedList;
    }

    public Suggestions setSectionedList(SectionedList sectionedList) {
        this.sectionedList = sectionedList;
        return this;
    }

    public class SectionedList implements Serializable {
        private static final long serialVersionUID = 1L;


        private List<Suggestion> properties;
        private List<Suggestion> cities;
        private List<Suggestion> landmarks;
        private List<Suggestion> districts;

        public SectionedList() {
            properties = new ArrayList<Suggestion>();
            cities = new ArrayList<Suggestion>();
            landmarks = new ArrayList<Suggestion>();
            districts = new ArrayList<Suggestion>();
        }

        public ArrayList<Suggestion> getProperties() {
            ArrayList<Suggestion> items = new ArrayList<Suggestion>();
            if (items.size() > 0) {
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getDecorator().isPropertyAPI()) {
                        items.add(items.get(i));
                    }
                }
            }
            properties = items;
            return items;
        }

        public ArrayList<Suggestion> getCities() {
            ArrayList<Suggestion> lCities = new ArrayList<Suggestion>();
            if (lCities.size() > 0) {
                for (int i = 0; i < cities.size(); i++) {
                    if (lCities.get(i).getDecorator().isCity()) {
                        lCities.add(lCities.get(i));
                    }
                }
            }
            cities = lCities;
            return lCities;
        }

        public ArrayList<Suggestion> getSectionedList() {
            splitDormsPrivate();
            ArrayList<Suggestion> result = new ArrayList<Suggestion>();
            if (cities.size() > 0) {
                Suggestion separ1 = new Suggestion();
                separ1.getDecorator().setSeparator(true);
                separ1.getDecorator().setType(CITY);
                result.add(separ1);
                result.addAll(cities);
            }
            if (properties.size() > 0) {
                Suggestion separ = new Suggestion();
                separ.getDecorator().setSeparator(true);
                separ.getDecorator().setType(PROPERTY);
                result.add(separ);
                result.addAll(properties);
            }
            if (districts.size() > 0) {
                Suggestion separ = new Suggestion();
                separ.getDecorator().setSeparator(true);
                separ.getDecorator().setType(DISTRICT);
                result.add(separ);
                result.addAll(districts);
            }
            if (landmarks.size() > 0) {
                Suggestion separ = new Suggestion();
                separ.getDecorator().setSeparator(true);
                separ.getDecorator().setType(LANDMARK);
                result.add(separ);
                result.addAll(landmarks);
            }


            suggestions = result;
            return result;
        }

        private void splitDormsPrivate() {
            for (int i = 0; i < suggestions.size(); i++) {
                Suggestion property = suggestions.get(i);
                if (property.getDecorator().isPropertyAPI()) {
                    property.getDecorator().setType(PROPERTY);
                    properties.add(property);
                }else  if(property.getDecorator().isDistrictAPI()){
                    property.getDecorator().setType(DISTRICT);
                    districts.add(property);
                }else  if(property.getDecorator().isLandmarkAPI()){
                    property.getDecorator().setType(LANDMARK);
                    landmarks.add(property);
                }else {
                    property.getDecorator().setType(CITY);
                    cities.add(property);
                }
            }
        }
    }

}
