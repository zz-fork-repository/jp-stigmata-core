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
import jp.naist.se.stigmata.filter.FilterTarget;
import jp.naist.se.stigmata.filter.TargetNameComparisonPairFilter;
import jp.naist.se.stigmata.filter.TargetNameComparisonPairFilterService;
import jp.naist.se.stigmata.spi.ComparisonPairFilterSpi;
import jp.naist.se.stigmata.ui.swing.StigmataFrame;

/**
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class TargetNameComparisonPairFilterComponentService extends AbstractComparisonPairFilterComponentService{

    public ComparisonPairFilterPane createComponent(StigmataFrame frame, ComparisonPairFilterSpi service){
        return new Pane(frame, service);
    }

    public String getFilterName(){
        return "name";
    }

    public ComparisonPairFilterSpi getComparisonPairFilterService(){
        return new TargetNameComparisonPairFilterService();
    }

    private static class Pane extends ComparisonPairFilterPane{
        private static final long serialVersionUID = 8912037614500713027L;
        private ComparisonPairFilterSpi service;
        private JComboBox criterionType;
        private JTextField value;
        private JComboBox targetType;

        public Pane(StigmataFrame frame, ComparisonPairFilterSpi service){
            super(frame);
            this.service = service;
            initLayouts();
        }

        @Override
        public String[] getErrors(){
            List<String> errors = new ArrayList<String>();
            if(value.getText().trim().equals("")){
                errors.add(getMessages().get("error.empty.value"));
            }

            return errors.toArray(new String[errors.size()]);
        }

        @Override
        public ComparisonPairFilter getFilter(){
            try{
                TargetNameComparisonPairFilter filter = new TargetNameComparisonPairFilter(service);
                filter.setCriterion(getCriterion((String)criterionType.getSelectedItem()));
                filter.setValue(value.getText());
                filter.setTarget(getTarget((String)targetType.getSelectedItem()));

                return filter;
            } catch(Exception e){
            }
            return null;
        }

        @Override
        public void resetComponents(){
            value.setText("");
            criterionType.setSelectedIndex(0);
            targetType.setSelectedItem(getDisplayTarget(FilterTarget.BOTH_TARGETS));
        }

        @Override
        public void setFilter(ComparisonPairFilter cpf){
            TargetNameComparisonPairFilter filter = (TargetNameComparisonPairFilter)cpf;
            criterionType.setSelectedItem(getDisplayCriterion(filter.getCriterion()));
            value.setText(filter.getValue());
            targetType.setSelectedItem(getDisplayTarget(filter.getTarget()));
        }

        private void initLayouts(){
            JLabel label = new JLabel(getMessages().get("filter.name.label"));
            value = new JTextField();
            criterionType = createCriteriaBox(TargetNameComparisonPairFilter.getValidCriteria());
            targetType = createTargetBox();

            setLayout(new GridLayout(4, 1));
            add(label);
            add(targetType);
            add(criterionType);
            add(value);
        }
    };
}
