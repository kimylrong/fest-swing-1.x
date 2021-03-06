/*
 * Created on Jul 19, 2009
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2009-2013 the original author or authors.
 */
package org.fest.swing.driver;

import static org.fest.swing.test.core.CommonAssertions.assertThatErrorCauseIsDisabledComponent;
import static org.fest.swing.test.core.CommonAssertions.assertThatErrorCauseIsNotShowingComponent;
import static org.fest.swing.test.core.CommonAssertions.failWhenExpectingException;
import static org.fest.swing.test.util.StopWatch.startNewStopWatch;

import java.util.concurrent.CountDownLatch;

import org.fest.swing.test.util.StopWatch;
import org.junit.Test;

/**
 * Tests for {@link ComponentDriver#focusAndWaitForFocusGain(java.awt.Component)}.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public class ComponentDriver_focusAndWaitForFocusGain_Test extends ComponentDriver_TestCase {
  @Test
  public void should_give_focus_to_Component_and_wait_till_it_is_focused() {
    showWindow();
    assertThatButtonDoesNotHaveFocus();
    window.button.waitToRequestFocus();
    final CountDownLatch done = new CountDownLatch(1);
    StopWatch stopWatch = startNewStopWatch();
    new Thread() {
      @Override
      public void run() {
        driver.focusAndWaitForFocusGain(window.button);
        done.countDown();
      }
    }.start();
    try {
      done.await();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    stopWatch.stop();
    assertThatButtonHasFocus();
    assertThatWaited(stopWatch, TIME_TO_WAIT_FOR_FOCUS_GAIN);
  }

  @Test
  public void should_throw_error_if_Component_is_disabled() {
    disableButton();
    try {
      driver.focusAndWaitForFocusGain(window.button);
      failWhenExpectingException();
    } catch (IllegalStateException e) {
      assertThatErrorCauseIsDisabledComponent(e);
    }
  }

  @Test
  public void should_throw_error_if_Component_is_not_showing_on_the_screen() {
    try {
      driver.focusAndWaitForFocusGain(window.button);
      failWhenExpectingException();
    } catch (IllegalStateException e) {
      assertThatErrorCauseIsNotShowingComponent(e);
    }
  }
}
