package com.interviewhub.collections;

import com.interviewhub.model.Candidate;
import com.interviewhub.model.Interview;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Milestone 2 Verification Test
 * Tests ArrayList, HashMap O(1) lookup, HashSet duplicate prevention,
 * Comparator-based sorting, filtering, and Top 5 candidates ranking.
 */
public class CollectionsTest {

    private CollectionManager manager;

    @BeforeEach
    public void setUp() {
        manager = new CollectionManager();
    }

    @Test
    public void testHashSetDuplicatePrevention() {
        Candidate c1 = new Candidate();
        c1.setCandidateId(1);
        c1.setUserId(101);
        c1.setName("Alice");
        c1.setEmail("alice@test.com");
        c1.setStudentId("STU01");

        Candidate c2 = new Candidate();
        c2.setCandidateId(2);
        c2.setUserId(102);
        c2.setName("Alice Duplicate");
        c2.setEmail("ALICE@TEST.COM"); // Duplicate email in different case
        c2.setStudentId("STU02");

        assertTrue(manager.addCandidate(c1), "First candidate should be added successfully");
        assertFalse(manager.addCandidate(c2), "Second candidate with duplicate email must be rejected by HashSet");
        assertTrue(manager.isEmailRegistered("alice@test.com"));
    }

    @Test
    public void testHashMapLookupAndIteratorRemoval() {
        Candidate c = new Candidate();
        c.setCandidateId(50);
        c.setUserId(500);
        c.setName("Charlie");
        c.setEmail("charlie@test.com");
        c.setStudentId("STU50");

        manager.addCandidate(c);

        // Fast O(1) Lookup
        Candidate retrieved = manager.findCandidateById(50);
        assertNotNull(retrieved);
        assertEquals("Charlie", retrieved.getName());

        // Iterator Removal
        assertTrue(manager.removeCandidateById(50));
        assertNull(manager.findCandidateById(50));
        assertFalse(manager.isEmailRegistered("charlie@test.com"));
    }

    @Test
    public void testComparatorSortingAndTopCandidates() {
        Candidate c1 = createCandidate(1, "Bob", 7.5, "IT");
        Candidate c2 = createCandidate(2, "Alice", 9.2, "CS");
        Candidate c3 = createCandidate(3, "David", 8.4, "CS");

        manager.addCandidate(c1);
        manager.addCandidate(c2);
        manager.addCandidate(c3);

        // Sort by CGPA descending
        List<Candidate> byCgpa = manager.sortCandidatesByCgpaDescending();
        assertEquals("Alice", byCgpa.get(0).getName());
        assertEquals("David", byCgpa.get(1).getName());
        assertEquals("Bob", byCgpa.get(2).getName());

        // Sort by Name alphabetical
        List<Candidate> byName = manager.sortCandidatesByName();
        assertEquals("Alice", byName.get(0).getName());
        assertEquals("Bob", byName.get(1).getName());
        assertEquals("David", byName.get(2).getName());

        // Add interviews with scores
        manager.addInterview(createInterview(101, 1, 60.0));
        manager.addInterview(createInterview(102, 2, 95.0));
        manager.addInterview(createInterview(103, 3, 85.0));

        List<Map.Entry<Candidate, Double>> top = manager.getTopFiveCandidatesByScore();
        assertEquals(3, top.size());
        assertEquals("Alice", top.get(0).getKey().getName());
        assertEquals(95.0, top.get(0).getValue());
        assertEquals("David", top.get(1).getKey().getName());
    }

    private Candidate createCandidate(int id, String name, double cgpa, String dept) {
        Candidate c = new Candidate();
        c.setCandidateId(id);
        c.setUserId(id + 100);
        c.setName(name);
        c.setEmail(name.toLowerCase() + "@college.edu");
        c.setStudentId("STU" + id);
        c.setCgpa(cgpa);
        c.setDepartment(dept);
        c.setSpecialization("General");
        return c;
    }

    private Interview createInterview(int invId, int candId, double score) {
        Interview inv = new Interview();
        inv.setInterviewId(invId);
        inv.setCandidateId(candId);
        inv.setInterviewerId(1);
        inv.setInterviewType("TECHNICAL");
        inv.setInterviewDate(LocalDate.now());
        inv.setInterviewTime(LocalTime.of(10, 0));
        inv.setInterviewStatus("COMPLETED");
        inv.setOverallScore(score);
        return inv;
    }
}
