export type ObjStringType = {
  [index: string]: string;
};

export type ObjNumberType = {
  [index : string] : number
}

export type JubtiType = {
  [index: string]: string | number;
  age: string;
  sex: string;
  level: number;
  tasteRefresh: number;
  tasteBody: number;
  tasteSweet: number;
  tasteSour: number;
  tasteThroat: number;
  tasteFlavor: number;
  dish: string;
};

export type DrinkTasteType = {
    [index: string] : number,
    tasteRefresh: number,
    tasteBody: number,
    tasteSweet: number,
    tasteSour: number,
    tasteThroat: number,
    tasteFlavor: number,
}

export type Drink = {
  [index: string]: number | string | boolean;
  drinkId: number;
  drinkName: string;
  drinkImage: string;
  drinkAmount: string;
  drinkLevel: number;
  like: boolean;
  popularity: number;
};

export type DrinkDetailType = {
    [index: string] : string | number | ReviewType[],
    drinkId: number,
    drinkName: string,
    drinkInfo: string,
    drinkImage: string,
    drinkSaleUrl: string,
    drinkPrice: string,
    drinkAmount: string,
    drinkLevel: number,
    drinkType: any,
    drinkReviews: ReviewType[],
}

export const DrinkDetailTypeFirst = {
    drinkId: 0,
    drinkName: '',
    drinkInfo: '',
    drinkImage: '',
    drinkSaleUrl: '',
    drinkPrice: '',
    drinkAmount: '',
    drinkLevel: 0,
    drinkType: '',
    drinkReviews: [],
}

export type ReviewType = {
    [index : string] : string | number,
    userName: string,
    userImage: string,
    rate : number,
    content: string
}

export type tasteType = {
    [index: string] : number,
    tasteFlavor: number,
    tasteSweet: number,
    tasteSour: number,
    tasteThroat: number,
    tasteBody: number,
    tasteRefresh: number,
}



export interface UserClear {
  [index : string] : number | string;
  drinkId: number;
  drinkName: string;
  drinkImage: string;
  drinkAmount: string;
  drinkLevel: string;
  drinkType: string;
}

export interface UserPreferenceStore {
  [index : string] : number | string | string[];
  storeId: number;
  storeName: string;
  storeImage: string;
  storeAddress: string;
  storeDrink: string[];
  storeDrinkImage: string[];
  storeDrinkType: string[];
}

export type todayCheers = { 
  todayId: number;
  todayName: string;
  todayContent: string;
}

export type todayDrink = { 
  todayId: number;
  todayLevel: number;
  todayDrink: string;
  todayType: string;
  todayAmount: string;
  todayImage: string;
}

export type todayDish = { 
  todayId: number;
  todayDish: string;
}

export type UserType = {
  userId: number
  kakaoId: string,
  nickname: string,
  age: number|null,
  sex: number|null,
  img: string,
  ranking: number,
  finish: boolean,
  likeDrinkCnt : number,
  likeJumakCnt : number,
  clearDrinkCnt : number,
  userPreferenceDto : null | UserPreferenceType
}

export type TextType = {
  text: string,
  value : number,
}

export type TextMinigType = {
  'words': TextType[]
}

export type UserPreferenceType = {
  age : string,
  sex : string,
  tasteSour: number,
  tasteSweet: number,
  tasteFlavor: number,
  tasteRefresh: number,
  tasteBody: number,
  tasteThroat: number,
  level: number,
  dish: string,
  weight: string,
}