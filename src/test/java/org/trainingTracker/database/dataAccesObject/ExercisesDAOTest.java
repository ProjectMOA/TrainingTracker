package org.trainingTracker.database.dataAccesObject;

import org.junit.Ignore;
import org.junit.Test;
import org.trainingTracker.database.valueObject.ExerciseVO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by sergio on 28/10/16.
 */
public class ExercisesDAOTest {

    @Test
    public void test() throws Exception {

        ExercisesDAO.addExercise("Flexiones","Brazo");
        ExercisesDAO.addExercise("Pesas","Brazo","JohnDoe1");


        List<ExerciseVO> list = ExercisesDAO.listExercises("JohnDoe1");

        for (ExerciseVO exercise:list ) {
            System.out.println(exercise.toString());
        }
    }

}
