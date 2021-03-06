package lotto.domain;

import java.util.ArrayList;
import java.util.List;

public class Lottos {
    public static final String NUMBER_TYPE_ERROR = "[ERROR] 숫자만 입력할 수 있습니다";
    public static final String NUMBER_RANGE_ERROR = "[ERROR] 입력하신 금액으로 구입할 수 없습니다";
    private final int manualCount;
    private final int randomCount;
    private final List<Lotto> lottoGroup = new ArrayList<>();

    public Lottos(Money money, String inputManualCount) {
        manualCount = validateManualCount(inputManualCount, money.count());
        randomCount = money.count() - manualCount;
        generateLottoGroup(randomCount);
    }

    private int validateManualCount(String inputManual, int totalCount) {
        int manualCount = validateType(inputManual);
        validateRange(manualCount, totalCount);
        return manualCount;
    }

    private int validateType(String inputManual) {
        try {
            return Integer.parseInt(inputManual);
        } catch (Exception e) {
            throw new IllegalArgumentException(NUMBER_TYPE_ERROR);
        }
    }

    private void validateRange(int manualCount, int totalCount) {
        if (manualCount < 0 || manualCount > totalCount) {
            throw new IllegalArgumentException(NUMBER_RANGE_ERROR);
        }
    }

    private void generateLottoGroup(int count) {
        LottoGenerator lottoGenerator = new LottoGenerator();
        for (int i = 0; i < count; i++) {
            Lotto generatedLotto = new Lotto(lottoGenerator.generateLottoNums());
            lottoGroup.add(generatedLotto);
        }
    }

    public Result drawLotto(WinningLotto winningLotto) {
        List<Rank> wins = new ArrayList<>();
        for (Lotto lotto : lottoGroup) {
            Rank rank = winningLotto.findRank(lotto);
            wins.add(rank);
        }
        return new Result(wins);
    }

    public ArrayList<ArrayList<Integer>> changeFormat() {
        ArrayList<ArrayList<Integer>> displayFormat = new ArrayList<>();
        for (Lotto lotto : lottoGroup) {
            displayFormat.add(lotto.changeToList());
        }
        return displayFormat;
    }

    public void generateManualLotto(List<String> manualNumberInput) {
        for(String manualNumber: manualNumberInput) {
            lottoGroup.add(Lotto.from(manualNumber));
        }
    }

    public boolean isManualCount(int count) {
        return manualCount == count;
    }

    public boolean isRandomCount(int count) {
        return randomCount == count;
    }
}