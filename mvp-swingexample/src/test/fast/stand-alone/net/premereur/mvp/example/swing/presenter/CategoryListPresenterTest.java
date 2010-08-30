package net.premereur.mvp.example.swing.presenter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.any;

import java.util.List;

import javax.swing.JFrame;

import net.premereur.mvp.example.swing.eventbus.DemoEventBus;
import net.premereur.mvp.example.swing.view.CategoryList;

import org.junit.Before;
import org.junit.Test;


public class CategoryListPresenterTest {

	private CategoryListPresenter presenter;
	private CategoryList view;
	private DemoEventBus eventBus;
	
	@Before
	public void setUpPresenterWithMockView() {
		view = mock(CategoryList.class);
		eventBus = mock(DemoEventBus.class);
		presenter = new CategoryListPresenter();
		presenter.setView(view);
		presenter.setEventBus(eventBus);
	}
	
	@Test
	public void shouldAddTheViewToTheApplicationFrame() throws Exception {
		JFrame frame = mock(JFrame.class);
		presenter.onCategoryListActivated(frame);
		verify(view).showInFrame(frame);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void shouldBindViewData() throws Exception {
		JFrame frame = mock(JFrame.class);
		presenter.onCategoryListActivated(frame);
		verify(view).bind(any(List.class));
	}
}
