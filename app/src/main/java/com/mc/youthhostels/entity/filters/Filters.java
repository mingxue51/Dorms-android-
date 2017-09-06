package com.mc.youthhostels.entity.filters;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entity.Property.Amenity;
import entity.Property.Property;
import helper.H;

public class Filters {

    private DormFilter dormFilter;
    private PrivateRoomsFilter privateRoomsFilter;
    private List<Filterable> typeFilters;
    private List<Filterable> amenityFilters;
    private Double selectedMinPrice;
    private Double selectedMaxPrice;
    private Double minPrice;
    private Double maxPrice;

    public void init(List<Property> properties) {
        if (properties == null) {
            return;
        }

        dormFilter = new DormFilter();
        privateRoomsFilter = new PrivateRoomsFilter();

        typeFilters = new ArrayList<>();
        amenityFilters = new ArrayList<>();

        initPropertyTypes(properties);
        initAmenityFilters(properties);
        initPrices(properties);
    }

    private void initPrices(List<Property> properties) {
        minPrice = getMinPrice(properties);
        maxPrice = getMaxPrice(properties);

        selectedMinPrice = minPrice;
        selectedMaxPrice = maxPrice;

        H.logD("filters init min price:" + minPrice);
        H.logD("filters init max price:" + maxPrice);
    }

    private void initAmenityFilters(List<Property> properties) {
        Set<String> amenitiesSet = new HashSet<>();
        for (Property property : properties) {
            for (Amenity amenity : property.getAmenitiesForProperty()) {
                amenitiesSet.add(amenity.getName());
            }
        }

        H.logD("amenities for properties found :" + amenitiesSet.size());

        for (String type: amenitiesSet) {
            amenityFilters.add(new AmenityFilter(type));
        }
    }

    private void initPropertyTypes(List<Property> properties) {
        Set<String> typesSet = new HashSet<>();
        for (Property property : properties) {
            typesSet.add(property.getPropertyType());
        }

        for (String type: typesSet) {
            typeFilters.add(new TypeFilter(type));
        }
    }

    public List<Property> filterAll(List<Property> properties) {
        List<Property> filtered = new ArrayList<>();

        H.logD("filters input:" + properties.size());

        boolean isAnyActivated = false;

        if (dormFilter.isActivated() || privateRoomsFilter.isActivated()) {
            filtered = filterOr(properties, Arrays.asList(dormFilter, privateRoomsFilter));
            isAnyActivated = true;
        }

        if (!isAnyActivated) {
            filtered = properties;
        }

        H.logD("after room types filters : " + filtered.size());

        if (isAnyActivated(typeFilters)) {
            filtered = filterOr(filtered, typeFilters);
            isAnyActivated = true;
        }

        if (!isAnyActivated) {
            filtered = properties;
        }

        H.logD("after property types filters : " + filtered.size());
        if (isAnyActivated(amenityFilters)) {
            filtered = filterAnd(filtered, amenityFilters);
            isAnyActivated = true;
        }

        if (!isAnyActivated) {
            filtered = properties;
        }

        H.logD("after amenities filters : " + filtered.size());

        // filtering by price
        List<Property> results = new ArrayList<>();
        for (Property property : filtered) {
            Double priceValue = property.getComparedPrice();
            if (priceValue >= selectedMinPrice && priceValue <= selectedMaxPrice) {
                results.add(property);
            }
        }

        H.logD("after price filters : " + results.size());

        return results;
    }

    public void apply() {
        dormFilter.apply();
        privateRoomsFilter.apply();
        apply(typeFilters);
        apply(amenityFilters);
    }

    public void sync() {
        dormFilter.sync();
        privateRoomsFilter.sync();
        sync(typeFilters);
        sync(amenityFilters);
    }

    private void apply(List<Filterable> filters) {
        for (Filterable filter : filters) {
            filter.apply();
        }
    }

    private void sync(List<Filterable> filters) {
        for (Filterable filter : filters) {
            filter.sync();
        }
    }

    private boolean isAnyActivated(List<Filterable> filters) {
        if (filters == null || filters.isEmpty()) {
            return false;
        }

        for (Filterable filter : filters) {
            if (filter.isActivated()) {
                return true;
            }
        }

        return false;
    }

    private List<Property> filterOr(List<Property> properties, List<Filterable> filters) {
        List<Property> filtered = new ArrayList<>();

        for (Property property : properties) {

            for (Filterable filter : filters) {
                if (filter.isPass(property) && filter.isActivated()) {
                    filtered.add(property);
                    break;
                }
            }
        }

        return filtered;
    }

    private List<Property> filterAnd(List<Property> properties, List<Filterable> filters) {
        H.logD("filters by and logic started");

        int activatedCount = 0;
        for (Filterable filter : filters) {
            if (filter.isActivated()) {
                activatedCount++;
            }
        }

        List<Property> filtered = new ArrayList<>();
        for (Property property : properties) {
            int count = 0;
            for (Filterable filter : filters) {
                if (filter.isPass(property) && filter.isActivated()) {
                    count++;
                }
            }
            if (count == activatedCount) {
                filtered.add(property);
                H.logD("property passed :" + property.getPropertyName());
            }
        }

        return filtered;
    }

    public List<Filterable> getTypeFilters() {
        return typeFilters;
    }

    public List<Filterable> getAmenityFilters() {
        return amenityFilters;
    }

    public List<Filterable> getRoomTypeFilters() {
        return Arrays.asList(dormFilter, privateRoomsFilter);
    }

    public Double getSelectedMinPrice() {
        return selectedMinPrice;
    }

    public static Double getMinPrice(List<Property> properties) {
        Double min = null;
        for (Property property : properties) {
            Double priceValue = property.getPriceValue();
            if (min == null) {
                min = priceValue;
            }

            if (min > priceValue) {
                min = priceValue;
            }
        }
        if (min == null) {
            return 0d;
        }
        return min;
    }

    public static Double getMaxPrice(List<Property> properties) {
        Double max = null;
        for (Property property : properties) {
            Double priceValue = property.getPriceValue();
            if (max == null) {
                max = priceValue;
            }

            if (max < priceValue) {
                max = priceValue;
            }
        }
        if (max == null) {
            return 0d;
        }
        return max;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public Double getSelectedMaxPrice() {
        return selectedMaxPrice;
    }

    public void setSelectedMinPrice(Double selectedMinPrice) {
        this.selectedMinPrice = selectedMinPrice;
    }

    public void setSelectedMaxPrice(Double selectedMaxPrice) {
        this.selectedMaxPrice = selectedMaxPrice;
    }
}
