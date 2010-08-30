package net.premereur.mvp.example.swing.presenter;

import javax.swing.JFrame;

import net.premereur.mvp.core.BasePresenter;
import net.premereur.mvp.core.UsesView;
import net.premereur.mvp.example.swing.eventbus.DemoEventBus;
import net.premereur.mvp.example.swing.model.DataProvider;
import net.premereur.mvp.example.swing.view.CategoryList;

@UsesView(CategoryList.class)
public class CategoryListPresenter extends BasePresenter<CategoryList, DemoEventBus> {

	public void onCategoryListActivated(JFrame frame) {
		getView().bind(DataProvider.allCategories());
		getView().showInFrame(frame);
	}
}