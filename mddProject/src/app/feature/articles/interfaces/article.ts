export interface Article {
    id: number;
    title: string ;
    content: string ;
    createdAt: Date ;
    updatedAt: Date ;
    userId: number ;
    themeId: number | null ;
    authorName: string ;
    themeTitle: string ;
    comments: Comment[]; // Commentaires de l'article;  // Liste des commentaires associés à l'article
}

export interface Comment {


    id: number;
    content: string;
    userId: number;
    authorName: string;
    articleId: number;

}