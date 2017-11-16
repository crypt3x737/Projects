
import java.awt.geom.Point2D;
import java.util.Vector;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 7275514
 */
public class MapMethods {

    public Vector<Bone_Description> boneLibrary;

    /**
     * Creates a new MapMethods object.
     * @param Vector<Bone_Description> bonelib - the vector with bone data
     * @return MapMethods
     */
    MapMethods(Vector<Bone_Description> boneLib) {
        boneLibrary = boneLib;

    }

    /**
     * Finds the elevation of each bone and uses this to find the max and min.
     * @param Vector<Bonerec> bone_records - an array of bonerec objects
     * @return none
     */
    
    public void findRangeOfElevations(Vector<Bonerec> bone_records) {
        double minElevation = Integer.MAX_VALUE;
        double maxElevation = Integer.MIN_VALUE;

        for (int i = 0; i < bone_records.size(); i++) {
            Bonerec currentBone = bone_records.get(i);
            double currentElevation = Double.parseDouble(currentBone.elevation);
            if (currentElevation < minElevation) {
                minElevation = currentElevation;
            } else if (currentElevation > maxElevation) {
                maxElevation = currentElevation;
            }
        }


    }

    /**
     * Finds the shape length of each bone and uses this to find the max and min.
     * @param Vector<Bonerec> bone_records - an array of bonerec objects
     * @return none
     */
    
    public void differentiateData(Vector<Bonerec> bone_records) {
        //        int minimumYear = Integer.MAX_VALUE;
        //        int maximumYear = Integer.MIN_VALUE;

        double minShapeLength = Double.MAX_VALUE;
        double maxShapeLength = Double.MIN_VALUE;
        
        for (int i = 0; i < bone_records.size(); i++) {
            Bonerec currentBone = bone_records.get(i);
            double currentlength = Double.parseDouble(currentBone.shape_length);
            if (currentlength > maxShapeLength) {
                maxShapeLength = currentlength;
            } else if (currentlength < minShapeLength) {
                minShapeLength = currentlength;
            }
        }
    }

    /**
     * Finds which bone is closest to the click if the click is within multiple.
     * @param Vector<Bonerec> bone_records - an array of bonerec objects
     * @param Vector<Bone_Description> bone_record - the bone data
     * @param Vector<String> boneResults - the bones the click was inside
     * @param Point2D.Double testPoint - the point that was clicked
     * @return Bonerec closestBone- the bone that is closest
     */
    public Bonerec findClosestBone(Vector<Bone_Description> descriptions,
            Vector<Bonerec> bone_records, Vector<String> boneResults, Point2D.Double testPoint) {      
        int index = -1;

        Bonerec closestBone = new Bonerec();
        // needs to be more efficient, but go through each result
        for (int i = 0; i < boneResults.size(); i++) {
            double minDistance = Double.MAX_VALUE;

            // and if that bone matches from the descriptions, do the calculation
            for (int j = 0; j < descriptions.size(); j++) {
                if (descriptions.get(j).id.equals(boneResults.get(i))) {
                    Double xCenter = descriptions.get(j).x_max - descriptions.get(j).x_min;
                    Double yCenter = descriptions.get(j).y_max - descriptions.get(j).y_min;
                    Double distance = testPoint.distance(xCenter, yCenter);
                    if (distance < minDistance) {
                        minDistance = distance; // reset minimum distance
                        index = i; // index of result

                        // find closest bone
                        for (int k = 0; k < bone_records.size(); k++) {
                            if (bone_records.get(k).unique_id.equals(boneResults.get(index))) {
                                closestBone = bone_records.get(k);
                            }
                        }
                    }
                }
            }
        }
        return closestBone;
    }

    /**
     * Finds if the click is within a given bone.
     * @param Point2D.Double testPoint - the point that was clicked
     * @return Vector<String> matchingBone - a list of bones the click is within
     */
    public Vector<String> pointWithinBone(Point2D.Double mouseClick) {
        Vector<String> matchingBones = new Vector<String>();


        for (int i = 0; i < boneLibrary.size(); i++) {
            Bone_Description currentBone = boneLibrary.get(i);// go thru each bone and check if it's within the bounding box
            
            if (withinBoundingBox(mouseClick, currentBone)) {
                matchingBones.add(currentBone.id);
            }

        }

        // for testing...
        // matchingBones.add("91HS053");
        return matchingBones;
    }



    /* edited from content from 
    http://stackoverflow.com/questions/217578/how-can-i-determine-whether-a-2d-point-is-within-a-polygon
     */
    public boolean withinBoundingBox(Point2D.Double mouseClick, Bone_Description currentBone) {
             //System.out.println( currentBone.y_min + " " + currentBone.y_max);
        // if it's not even in the bounding box we won't mess with it
        if (mouseClick.getX() < currentBone.x_min || mouseClick.getX() > currentBone.x_max
                || mouseClick.getY() < currentBone.y_min || mouseClick.getY() > currentBone.y_max) {
            return false;
        } else {
            return true;
        }
    }

}
