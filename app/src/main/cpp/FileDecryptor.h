//
// Created by vadimcg on 13.01.17.
//

#ifndef NDKEXPERIMENTS_FILEDECRYPTOR_H
#define NDKEXPERIMENTS_FILEDECRYPTOR_H


class FileDecryptor {

private:

    FileDecryptor(){}

public:
    static FileDecryptor& Instance() {
        static FileDecryptor instance;
        return instance;
    }
};


#endif //NDKEXPERIMENTS_FILEDECRYPTOR_H
