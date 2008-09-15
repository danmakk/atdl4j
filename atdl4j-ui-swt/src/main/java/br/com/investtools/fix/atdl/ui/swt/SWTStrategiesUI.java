package br.com.investtools.fix.atdl.ui.swt;

import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import br.com.investtools.fix.atdl.core.xmlbeans.StrategiesDocument;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategyT;
import br.com.investtools.fix.atdl.core.xmlbeans.StrategiesDocument.Strategies;
import br.com.investtools.fix.atdl.ui.StrategiesUI;
import br.com.investtools.fix.atdl.ui.StrategyUI;
import br.com.investtools.fix.atdl.ui.swt.validation.EditUI;
import br.com.investtools.fix.atdl.valid.xmlbeans.EditDocument.Edit;

public class SWTStrategiesUI implements StrategiesUI {

	private Map<StrategyT, StrategyUI> strategyUI;

	public SWTStrategiesUI(StrategiesDocument strategiesDocument,
			TabFolder tabFolder) throws XmlException {
		Strategies strategies = strategiesDocument.getStrategies();

		// create repository for global rules
		Map<String, EditUI> strategiesRules = new HashMap<String, EditUI>();

		for (Edit edit : strategies.getEditArray()) {
			String id = edit.getId();
			if (id != null) {
				EditUI rule = RuleFactory.createRule(edit, strategiesRules);
				strategiesRules.put(id, rule);
			} else {
				throw new XmlException("Strategies-scoped edit without id");
			}
		}

		// create repository for StrategyUI objects
		strategyUI = new HashMap<StrategyT, StrategyUI>();

		for (StrategyT strategy : strategies.getStrategyArray()) {
			// create tab item for strategy
			// TODO later: do not impose tab folder
			TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
			tabItem.setText(getText(strategy));
			Composite strategyComposite = new Composite(tabFolder, SWT.NONE);
			strategyComposite.setLayout(new FillLayout());
			tabItem.setControl(strategyComposite);

			// create strategy UI representation
			strategyUI.put(strategy, new SWTStrategyUI(strategy,
					strategiesRules, strategyComposite));
		}
	}

	private static String getText(StrategyT strategy) {
		if (strategy.getUiRep() != null) {
			return strategy.getUiRep();
		}
		return strategy.getName();
	}

	@Override
	public StrategyUI getStrategyUI(StrategyT strategy) {
		return strategyUI.get(strategy);
	}

}
