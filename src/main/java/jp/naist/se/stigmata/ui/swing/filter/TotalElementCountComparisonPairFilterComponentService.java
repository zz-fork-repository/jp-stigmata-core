package jp.naist.se.stigmata.ui.swing.filter;

/*
 * $Id$
 */

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import jp.naist.se.stigmata.ComparisonPairFilter;
import jp.naist.se.stigmata.filter.Target;
import jp.naist.se.stigmata.filter.TotalElementCountComparisonPairFilter;
import jp.naist.se.stigmata.filter.TotalElementCountComparisonPairFilterService;
import jp.naist.se.stigmata.spi.ComparisonPairFilterSpi;
import jp.naist.se.stigmata.ui.swing.Messages;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class TotalElementCountComparisonPairFilterComponentService extends AbstractComparisonPairFilterComponentService{

    public ComparisonPairFilterPane createComponent(ComparisonPairFilterSpi service){
        return new Pane(service);
    }

    public String getFilterName(){
        return "totalelementcount";
    }

    public ComparisonPairFilterSpi getComparisonPairFilterService(){
        return new TotalElementCountComparisonPairFilterService();
    }

    private static class Pane extends ComparisonPairFilterPane{
        private static final long serialVersionUID = 8912037614500713027L;
        private ComparisonPairFilterSpi service;
        private JComboBox criterionType;
        private JTextField threshold;
        private JComboBox targetType;

        public Pane(ComparisonPairFilterSpi service){
            this.service = service;
            initLayouts();
        }

        @Override
        public String[] getErrors(){
            List<String> errors = new ArrayList<String>();
            if(threshold.getText().trim().equals("")){
                errors.add(Messages.getString("error.empty.threshold"));
            }
            try{
                int v = Integer.parseInt(threshold.getText());
                if(v < 0){
                    errors.add(Messages.getString("error.negative.value", v));
                }
            } catch(NumberFormatException e){
                errors.add(Messages.getString("error.invalid.format.integer", threshold.getText()));
            }
            return errors.toArray(new String[errors.size()]);
        }

        @Override
        public ComparisonPairFilter getFilter(){
            try{
                TotalElementCountComparisonPairFilter filter = new TotalElementCountComparisonPairFilter(service);
                filter.setCriterion(getCriterion((String)criterionType.getSelectedItem()));
                filter.setThreshold(Integer.parseInt(threshold.getText()));
                filter.setTarget(getTarget((String)targetType.getSelectedItem()));

                return filter;
            } catch(NumberFormatException e){
            }
            return null;
        }

        @Override
        public void resetComponents(){
            threshold.setText("");
            criterionType.setSelectedIndex(0);
            targetType.setSelectedItem(getDisplayTarget(Target.BOTH_TARGET));
        }

        @Override
        public void setFilter(ComparisonPairFilter cpf){
            TotalElementCountComparisonPairFilter filter = (TotalElementCountComparisonPairFilter)cpf;
            criterionType.setSelectedItem(getDisplayCriterion(filter.getCriterion()));
            threshold.setText(String.valueOf(filter.getThreshold()));
            targetType.setSelectedItem(getDisplayTarget(filter.getTarget()));
        }

        private void initLayouts(){
            JLabel label = new JLabel(Messages.getString("filter.totalelementcount.label"));
            threshold = new JTextField();
            criterionType = createCriteriaBox(TotalElementCountComparisonPairFilter.CRITERIA);
            targetType = createTargetBox();

            setLayout(new GridLayout(4, 1));
            add(label);
            add(targetType);
            add(criterionType);
            add(threshold);
        }
    };
}
