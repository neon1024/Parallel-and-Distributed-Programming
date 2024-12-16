import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Polynomial {
    private static final int RANDOM_INT_UPPER_BOUND = 10;
    private final List<Integer> coefficients;

    // coefficients are given
    public Polynomial(List<Integer> coefficients) {
        this.coefficients = coefficients;
    }

    // degree is given
    public Polynomial(int degree) {
        coefficients = new ArrayList<>(degree + 1);

        // generate the coefficients
        Random randomGenerator = new Random();

        for (int i = 0; i < degree; i++) {
            coefficients.add(randomGenerator.nextInt(RANDOM_INT_UPPER_BOUND));
        }
        
        // the coefficient of the biggest power has to be different from 0
        int last_coefficient = randomGenerator.nextInt(RANDOM_INT_UPPER_BOUND);

        coefficients.add(last_coefficient == 0 ? 1 : last_coefficient);
    }

    public int getDegree() {
        return this.coefficients.size() - 1;
    }

    public int getLength() {
        return this.coefficients.size();
    }

    public List<Integer> getCoefficients() {
        return coefficients;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        int power = getDegree();

        for (int i = getDegree(); i >= 0; i--) {
            if (coefficients.get(i) == 0)
                continue;

            str.append(" ").append(coefficients.get(i)).append("x^").append(power).append(" +");
            power--;
        }

        str.deleteCharAt(str.length() - 1);  // delete last +

        return str.toString();
    }
}
