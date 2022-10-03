#!/bin/sh

# Test de l'interface en ligne de commande de decac.
# On ne met ici qu'un test trivial, a vous d'en ecrire de meilleurs.

cd "$(dirname "$0")"/../../.. || exit 1
ARG1=${1:-foo}

PATH=./src/main/bin:"$PATH"

test_decac_onfile=$(pwd)/src/test/context/valid/println.deca
text_file=$(pwd)/docs/liste-equipes.txt



#Vérification que decac -b fonctionne

decac_moins_b=$(decac -b)
decac_moins_b_file="(decac -b ${test_decac_onfile})"

if [ "$?" -ne 0 ]; then
    echo "ERREUR: decac -b a termine avec un status different de zero."
    if [ "$ARG1" != "-n" ]; then
            exit 1
    fi
fi

if [ "$decac_moins_b" = "" ]; then
    echo "ERREUR: decac -b n'a produit aucune sortie"
    if [ "$ARG1" != "-n" ]; then
            exit 1
    fi
fi

if echo "$decac_moins_b" | grep -i -q -e "erreur" -e "error"; then
    echo "ERREUR: La sortie de decac -b contient erreur ou error"
    if [ "$ARG1" != "-n" ]; then
            exit 1
    fi
fi

if ! eval ${decac_moins_b_file} 2>&1 | grep -i -q -e "No Other Options nor files"; then 
    echo "ERREUR: erreur decac -b avec un fichier prévu n'était pas levé"
    if [ "$ARG1" != "-n" ]; then
            exit 1
    fi
    else echo "Pas de probleme detecte avec decac -b."
fi



#Vérification que decac fonctionne

decac="decac"
decac_on_txt="decac ${text_file}"


if ! eval ${decac} 2>&1 | grep -i -q -e "affiche une bannière indiquant le nom de l’équipe" -e "arrête decac après l'étape de construction de l'arbre"; then 
    echo "ERREUR: decac doit afficher les options disponibles"
    if [ "$ARG1" != "-n" ]; then
            exit 1
    fi
fi

if ! eval ${decac_on_txt} 2>&1 | grep -i -q -e "give a deca filename" ; then 
    echo "ERREUR: decac doit fonctionner qu'avec les fichiers .deca"
    if [ "$ARG1" != "-n" ]; then
            exit 1
    fi
    else echo "Pas de probleme detecte avec decac."
fi




#Verification de l'incompatibilité des options
decac_p_v="decac -p -v ${test_decac_onfile}"
if ! eval ${decac_p_v} 2>&1 | grep -i -q -e "Error during option parsing"; then 
    echo "ERREUR: ça doit lever une erreur puisque les options -p -v decac sont incompatibles  "
    if [ "$ARG1" != "-n" ]; then
            exit 1
    fi 
    else echo "L'incompatibilité des options -p et -v est bien gérée "
fi 



#Vérification de l'option -d

decac_moins_d="decac -d ${test_decac_onfile}"
decac_moins_dd="decac -d -d ${test_decac_onfile}"
decac_moins_ddd="decac -d -d -d ${test_decac_onfile}"
decac_moins_dddd="decac -d -d -d -d ${test_decac_onfile}"

if ! eval ${decac_moins_d} 2>&1 | grep -i -q -e "level set to INFO"; then
    echo "ERREUR: erreur à lever au niveau de trace level INFO "
    if [ "$ARG1" != "-n" ]; then
            exit 1
    fi
    else echo "L'option decac -d validée"
fi


if ! eval ${decac_moins_dd} 2>&1 | grep -i -q -e "level set to DEBUG"; then
    echo "ERREUR: erreur à lever au niveau de trace level DEBUG"
    if [ "$ARG1" != "-n" ]; then
            exit 1
    fi
    else echo "L'option decac -d -d validée"
fi


if ! eval ${decac_moins_ddd} 2>&1 | grep -i -q -e "level set to TRACE"; then
    echo "ERREUR: erreur à lever au niveau de trace level TRACE"
    if [ "$ARG1" != "-n" ]; then
            exit 1
    fi
    else echo "L'option decac -d -d -d validée"
fi


if ! eval ${decac_moins_dddd} 2>&1 | grep -i -q -e "level set to ALL"; then
    echo "ERREUR: erreur à lever au niveau de trace level ALL"
    if [ "$ARG1" != "-n" ]; then
            exit 1
    fi
    else echo "L'option decac -d -d -d -d validée"
    
fi

#Vérification de l'option -r

decac_r3="decac -r 3 ${test_decac_onfile}"
decac_r17="decac -r 17 ${test_decac_onfile}"

if ! eval ${decac_r3} 2>&1 | grep -i -q -e "The number you are giving isn't born between 4 and 16!"; then
    echo "ERREUR: decac -r doit vérifier doit vérifier si le nombre est bien entre 4 et 16"
    if [ "$ARG1" != "-n" ]; then
            exit 1
    fi
fi

if ! eval ${decac_r17} 2>&1 | grep -i -q -e "The number you are giving isn't born between 4 and 16!"; then
    echo "ERREUR: decac -r doit vérifier doit vérifier si le nombre est bien entre 4 et 16"
    if [ "$ARG1" != "-n" ]; then
            exit 1
    fi

    else echo "Le test de l'option decac -r X validé"
fi

#Vérification de l'option -p


decac_moins_p="decac -p ${test_decac_onfile}"

if eval ${decac_d} 2>&1 | grep -i -q -e "erreur" -e "error" -e "exception"; then
    echo "ERREUR: erreur au niveau de l'option -p "
    if [ "$ARG1" != "-n" ]; then
            exit 1
    fi
    else echo "L'option decac -p validée"
fi




















# ... et ainsi de suite.
