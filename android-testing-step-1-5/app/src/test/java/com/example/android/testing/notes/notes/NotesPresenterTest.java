/*
 * Copyright 2015, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.testing.notes.notes;

import com.example.android.testing.notes.data.Note;
import com.example.android.testing.notes.data.NotesRepository;
import com.example.android.testing.notes.data.NotesRepository.LoadNotesCallback;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

/**
 * Unit tests for the implementation of {@link NotesPresenter}
 */
public class NotesPresenterTest {

    // CEK: mocked up list data...
    private static List<Note> NOTES = Lists.newArrayList(new Note("Title1", "Description1"),
            new Note("Title2", "Description2"));

    private static List<Note> EMPTY_NOTES = new ArrayList<>(0);

    @Mock
    private NotesRepository mNotesRepository;

    @Mock
    private NotesContract.View mNotesView;

    /**
     * {@link ArgumentCaptor} is a powerful Mockito API to capture argument values and use them to
     * perform further actions or assertions on them.
     */
    @Captor
    private ArgumentCaptor<LoadNotesCallback> mLoadNotesCallbackCaptor;

    //
    // CEK:  interesting -- the class under test is instantiated in the following class data member,
    // but there is no 'annotation' associated with this class variable (unlike..
    // .. unlink other vars with "@Captor" and "@Mock" and "@Before" and "@Test".
    private NotesPresenter mNotesPresenter;

    @Before
    public void setupNotesPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mNotesPresenter = new NotesPresenter(mNotesRepository, mNotesView);
    }

    @Test
    public void loadNotesFromRepositoryAndLoadIntoView() {
//        fail("Implement in step 6");
        // Given an initialized NotesPresenter with initialized notes
        // When loading of Notes is requested
        mNotesPresenter.loadNotes(true);
        // CEK: since we're checking the 'loading' indicator is stopped at the end of this "Test"
        // CEK:  then I want to test that it was started here:
        verify(mNotesView).setProgressIndicator(true);

        // Callback is captured and invoked with stubbed notes
        // CEK: 1. verify (<class-with_method_to_call)
        // CEK:   . <the-method-of-the-class>
        // CEK:   ( arg = <the-captor-that-captures-the-method's-argument-used-in-the-class-under-test>
        // CEK:        and captor's ".capture()" method )
        // CEK: 2. then use the captured data (which is a 'callback function'):
        // CEK:        call the callback function
        // CEK:          with Mocked data "NOTES" from this Test class above.
        // CEK 1:
        verify(mNotesRepository).getNotes(mLoadNotesCallbackCaptor.capture());
        // CEK 2:
        mLoadNotesCallbackCaptor.getValue().onNotesLoaded(NOTES);

        // Then progress indicator is hidden and notes are shown in UI
//        InOrder inOrder = Mockito.inOrder(mNotesView);
//        inOrder.verify(mNotesView).setProgressIndicator(true);
//        inOrder.verify(mNotesView).setProgressIndicator(false);
//        verify(mNotesView).showNotes(NOTES);
        verify(mNotesView).setProgressIndicator(false);
        verify(mNotesView).showNotes(NOTES);
    }

    @Test
    public void clickOnFab_ShowsAddsNoteUi() {
//        fail("Implement in step 6");
        // When adding a new note
        mNotesPresenter.addNewNote();

        // Then add note UI is shown
        verify(mNotesView).showAddNote();
    }

    @Test
    public void clickOnNote_ShowsDetailUi() {
        // Given a stubbed note
        Note requestedNote = new Note("Details Requested", "For this note");

        // When open note details is requested
        mNotesPresenter.openNoteDetails(requestedNote);

        // Then note detail UI is shown
        verify(mNotesView).showNoteDetailUi(any(String.class));
    }
}
