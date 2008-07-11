package jp.sourceforge.stigmata.ui.swing.filter;

/*
 * $Id$
 */

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import jp.sourceforge.stigmata.ComparisonPairFilter;
import jp.sourceforge.stigmata.filter.FilterTarget;
import jp.sourceforge.stigmata.filter.TotalElementCountComparisonPairFilter;
import jp.sourceforge.stigmata.filter.TotalElementCountComparisonPairFilterService;
import jp.sourceforge.stigmata.spi.ComparisonPairFilterSpi;
import jp.sourceforge.stigmata.ui.swing.StigmataFrame;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ 
 */
public class TotalElementCountComparisonPairFilterComponentService extends AbstractComparisonPairFilterComponentService{

    public ComparisonPairFilterPane createComponent(StigmataFrame frame, ComparisonPairFilterSpi service){
        return new Pane(frame, service);
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

        public Pane(StigmataFrame frame, ComparisonPairFilterSpi service){
            super(frame);
            this.service = service;
            initLayouts();
        }

        @Override
        public String[] getErrors(){
            List<String> errors = new ArrayList<String>();
            if(threshold.getText().trim().equals("")){
                errors.add(getMessages().get("error.empty.threshold"));
            }
            try{
                int v = Integer.parseInt(threshold.getText());
                if(v < 0){
                    errors.add(getMessages().format("error.negative.value", v));
                }
            } catch(NumberFormatException e){
                errors.add(getMessages().format("error.invalid.format.integer", threshold.getText()));
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
            targetType.setSelectedItem(getDisplayTarget(FilterTarget.BOTH_TARGETS));
        }

        @Override
        public void setFilter(ComparisonPairFilter cpf){
            TotalElementCountComparisonPairFilter filter = (TotalElementCountComparisonPairFilter)cpf;
            criterionType.setSelectedItem(getDisplayCriterion(filter.getCriterion()));
            threshold.setText(String.valueOf(filter.getThreshold()));
            targetType.setSelectedItem(getDisplayTarget(filter.getTarget()));
        }

        private void initLayouts(){
            JLabel label = new JLabel(getMessages().get("filter.totalelementcount.label"));
            threshold = new JTextField();
            criterionType = createCriteriaBox(TotalElementCountComparisonPairFilter.getValidCriteria());
            targetType = createTargetBox();

            setLayout(new GridLayout(4, 1));
            add(label);
            add(targetType);
            add(criterionType);
            add(threshold);
        }
    };
}
