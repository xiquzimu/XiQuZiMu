package me.xlgp.xiquzimu.config;

public class FetchRepositoryConfig {
    public static final String NAME = "FetchRepositoryConfig";
    public static final String REPOSITORY = "repository";

    private static Integer repositoryType = 0;

    public enum REPOSITORY_ENUM{
        GITEE,
        GITHUB,
    }

    public static boolean containEnum(REPOSITORY_ENUM repositoryEnum){
        FetchRepositoryConfig.REPOSITORY_ENUM[] repositoryEnums = FetchRepositoryConfig.REPOSITORY_ENUM.values();
        boolean b = false;
        for (FetchRepositoryConfig.REPOSITORY_ENUM repository_enum:repositoryEnums) {
            if (repository_enum == repositoryEnum) {
                b = true;
                break;
            }
        }
        return b;
    }

    public static REPOSITORY_ENUM getRepositoryType() {
        FetchRepositoryConfig.REPOSITORY_ENUM[] repositoryEnums = FetchRepositoryConfig.REPOSITORY_ENUM.values();
        for (FetchRepositoryConfig.REPOSITORY_ENUM repository_enum:repositoryEnums) {
           if (repository_enum.ordinal() == repositoryType) return repository_enum;
        }
        return REPOSITORY_ENUM.GITEE;
    }

    public static void setRepositoryType(Integer repositoryType) {
        FetchRepositoryConfig.repositoryType = repositoryType;
    }
}
