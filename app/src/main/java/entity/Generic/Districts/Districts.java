package entity.Generic.Districts;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import entity.Property.Property;

public class Districts implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<District> districts;
    private Map<Integer, Wrapper> districtsNew;

    public Districts(){
        districts = new ArrayList<District>();
        districtsNew = new HashMap<Integer, Wrapper>();
    }

    public List<District> getDistricts() {
        return districts;
    }

    public Districts setDistricts(List<District> districts) {
        this.districts = districts;
        return this;
    }

    public Map<Integer, Wrapper> getDistrictsNew() {
        return districtsNew;
    }

    public Districts setDistrictsNew(Map<Integer, Wrapper> districtsNew) {
        this.districtsNew = districtsNew;
        return this;
    }

    public static String getStringValue(ArrayList<District> items){
        String result = "";
        if(items.size()>0){
            StringBuilder localStringBuilder = new StringBuilder();

            Iterator localIterator = items.iterator();
            while (localIterator.hasNext()) {
                District entity = (District) localIterator.next();
                localStringBuilder.append(result).append("• ").append(entity.getName());
                if ( localIterator.hasNext()) {
                    localStringBuilder.append("\n");
                }
            }
            result = localStringBuilder.toString();
        }
        return result;
    }


    public Districts setDistricts(Property property) {

        List<District> landmarksForProperty = property.getDistricts().getDistricts();

        for (District item: landmarksForProperty){
            Integer id = item.getDistrict_id();
            if (!districtsNew.containsKey(id)) {
                districtsNew.put(id, new Wrapper(1,item));
            } else {
                districtsNew.put(id, districtsNew.get(id).increment());
            }
        }
        return this;
    }

    public class Wrapper implements Serializable {
        private static final long serialVersionUID = 1L;

        private Integer number;
        private District district;

        public Wrapper() {
            number = 0;
            district = new District();
        }

        public Wrapper(Integer pNumber, District pDistrict) {
            number = pNumber;
            district = pDistrict;
        }

        public Integer getNumber() {
            return number;
        }

        public Wrapper setNumber(Integer number) {
            this.number = number;
            return this;
        }

        public District getDistrict() {
            return district;
        }

        public Wrapper setDistrict(District district) {
            this.district = district;
            return this;
        }

        public Wrapper increment(){
            number++;
            return this;
        }
    }

}
