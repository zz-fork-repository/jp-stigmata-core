package jp.naist.se.stigmata.ui.swing.filter;

/*
 * $Id$
 */

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import jp.naist.se.stigmata.ComparisonPairFilter;
import jp.naist.se.stigmata.filter.BirthmarkElementCountComparisonPairFilter;
import jp.naist.se.stigmata.filter.BirthmarkElementCountComparisonPairFilterService;
import jp.naist.se.stigmata.filter.Target;
import jp.naist.se.stigmata.spi.BirthmarkSpi;
import jp.naist.se.stigmata.spi.ComparisonPairFilterSpi;
import jp.naist.se.stigmata.ui.swing.BirthmarkServiceListCellRenderer;
import jp.naist.se.stigmata.ui.swing.BirthmarkServiceListener;
import jp.naist.se.stigmata.ui.swing.Messages;

/**
 * 
 * @author Haruaki TAMADA
 * @version $Revision$ $Date$
 */
public class BirthmarkElementCountComparisonPairFilterComponentService extends AbstractComparisonPairFilterComponentService implements BirthmarkServiceListener{
    private Pane pane;

    public ComparisonPairFilterPane createComponent(ComparisonPairFilterSpi service){
        pane = new Pane(service);
        return pane;
    }

    public String getFilterName(){
        return "elementcount";
    }

    public void serviceAdded(BirthmarkSpi service){
        pane.serviceAdded(service);
    }

    public void serviceRemoved(BirthmarkSpi service){
        pane.serviceRemoved(service);
    }

    public ComparisonPairFilterSpi getComparisonPairFilterService(){
        return new BirthmarkElementCountComparisonPairFilterService();
    }

    private static class Pane extends ComparisonPairFilterPane implements BirthmarkServiceListener{
        private static final long serialVersionUID = -6398073942592186671L;

        private ComparisonPairFilterSpi service;
        private JComboBox criterionType;
        private JTextField threshold;
        private JComboBox targetType;
        private JComboBox birthmarks;

        public Pane(ComparisonPairFilterSpi service){
            this.service = service;
            initLayouts();
        }

        public void serviceAdded(BirthmarkSpi service){
            birthmarks.addItem(service);
        }

        public void serviceRemoved(BirthmarkSpi service){
            birthmarks.removeItem(service);
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
                BirthmarkElementCountComparisonPairFilter filter = new BirthmarkElementCountComparisonPairFilter(service);
                filter.setBirthmarkType(getBirthmarkType());
                filter.setCriterion(getCriterion((String)criterionType.getSelectedItem()));
                filter.setThreshold(Integer.parseInt(threshold.getText()));
                filter.setTarget(getTarget((String)targetType.getSelectedItem()));

                return filter;
            } catch(Exception e){
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
            BirthmarkElementCountComparisonPairFilter filter = (BirthmarkElementCountComparisonPairFilter)cpf;
            criterionType.setSelectedItem(getDisplayCriterion(filter.getCriterion()));
            threshold.setText(String.valueOf(filter.getThreshold()));
            targetType.setSelectedItem(getDisplayTarget(filter.getTarget()));
        }

        private void initLayouts(){
            JLabel label = new JLabel(Messages.getString("filter.elementcount.label"));
            threshold = new JTextField();
            criterionType = createCriteriaBox(BirthmarkElementCountComparisonPairFilter.getValidCriteria());
            birthmarks = new JComboBox();
            birthmarks.setRenderer(new BirthmarkServiceListCellRenderer(new Dimension(200, 20), 60));
            JLabel label2 = new JLabel(Messages.getString("filter.elementcount.label.next"));
            targetType = createTargetBox();

            setLayout(new GridLayout(6, 1));
            add(label);
            add(birthmarks);
            add(label2);
            add(targetType);
            add(criterionType);
            add(threshold);
        }

        private String getBirthmarkType(){
            BirthmarkSpi service = (BirthmarkSpi)birthmarks.getSelectedItem();
            if(service != null){
                return service.getType();
            }
            throw new IllegalStateException("invalid birthmarks");
        }
    }
}
