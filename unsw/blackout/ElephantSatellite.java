package unsw.blackout;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class ElephantSatellite extends Satellite {
    private static final double ELEPHANT_SPEED = 2500;
    private static final double ELEPHANT_RANGE = 400000;

    private static final int ELEPHANT_TRANSFER_SPEED = 20;
    private static final int ELEPHANT_MAX_BYTE = 90;

    public ElephantSatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, position, height, ELEPHANT_RANGE, MathsHelper.CLOCKWISE, ELEPHANT_SPEED,
                ELEPHANT_TRANSFER_SPEED, ELEPHANT_TRANSFER_SPEED, Integer.MAX_VALUE, ELEPHANT_MAX_BYTE);
        super.addUnsupportedDevice("DesktopDevice");
        super.addUnsupportedDevice("TeleportingSatellite");
    }

    @Override
    public boolean isMaxByteReached(int newFileSize) {
        List<File> files = getFiles();
        return files.stream().filter(file -> !file.isTransient()).mapToInt(File::getFileSize).sum()
                + newFileSize > ELEPHANT_MAX_BYTE;
    }

    @Override
    public void addFile(String filename, String content, boolean isFileComplete, int fileSize) {
        if (remainingBytes() < 0) {
            deleteTransientFiles();
        }

        super.addFile(filename, content, isFileComplete, fileSize);
    }

    private int remainingBytes() {
        List<File> files = getFiles();
        return ELEPHANT_MAX_BYTE - files.stream().mapToInt(File::getFileSize).sum();
    }

    private void deleteTransientFiles() {
        List<File> deleFiles = solveKnapsackProblem(remainingBytes());
        List<File> files = getFiles();

        files.stream().filter(file -> deleFiles.contains(file)).forEach(file -> file.setTransient(false));
        files = files.stream().filter(file -> !deleFiles.contains(file)).collect(Collectors.toList());

    }

    private List<File> solveKnapsackProblem(int availableSpace) {
        List<File> files = getFiles().stream().filter(file -> file.isTransient()).collect(Collectors.toList());

        int[][] dp = new int[files.size() + 1][availableSpace + 1];
        for (int i = 0; i <= files.size(); i++) {
            for (int w = 0; w <= availableSpace; w++) {
                if (i == 0 || w == 0) {
                    dp[i][w] = 0;
                } else if (files.get(i - 1).getFileSize() <= w) {
                    dp[i][w] = Math.max(
                            files.get(i - 1).getData().length() + dp[i - 1][w - files.get(i - 1).getFileSize()],
                            dp[i - 1][w]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        List<File> remainingFiles = new ArrayList<File>();

        int i = files.size();
        int w = availableSpace;

        while (i > 0 && w > 0) {
            if (dp[i][w] != dp[i - 1][w]) {
                remainingFiles.add(files.get(i - 1));
                w = w - files.get(i - 1).getFileSize();
            }
            i--;
        }

        List<File> deletFiles = files.stream().filter(file -> !remainingFiles.contains(file))
                .collect(Collectors.toList());

        return deletFiles;
    }
}
