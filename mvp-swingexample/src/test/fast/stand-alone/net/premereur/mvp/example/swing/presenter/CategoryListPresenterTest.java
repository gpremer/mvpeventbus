package net.premereur.mvp.example.swing.presenter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
		presenter = new CategoryListPresenter();
		presenter.setView(view);
	}
	
	@Test
	public void shouldAddTheViewToTheApplicationFrame() throws Exception {
		JFrame frame = mock(JFrame.class);
		presenter.onCategoryListActivated(frame);
		verify(frame).setContentPane(view);
	}
}
