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
    @Ignore
    public void addExercise() throws Exception {

        ExercisesDAO.addExercise("Flexiones","Brazo");
    }

    @Test
    @Ignore
    public void listAllExercises() throws Exception {

        List<ExerciseVO> list = ExercisesDAO.listAllExercises();

        for (ExerciseVO exercise:list ) {
            System.out.println(exercise.toString());
        }
    }

}
